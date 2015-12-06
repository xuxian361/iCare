package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.history.HistoryItem;
import com.google.zxing.client.android.history.HistoryManager;
import com.google.zxing.client.android.result.ResultHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

/**
 * Created by casum on 14-3-31.
 */
public class MyQRCapture implements SurfaceHolder.Callback {

    private Activity context;
    private boolean hasSurface;
    private HistoryManager historyManager;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;
    private CameraManager cameraManager;
    private ViewfinderView viewfinderView;
    private CaptureActivityHandler handler;
    private Result lastResult;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private Map<DecodeHintType, ?> decodeHints;
    private String sourceUrl;
    private static final String[] ZXING_URLS = {"http://zxing.appspot.com/scan", "zxing://scan/"};
    private ScanFromWebPageManager scanFromWebPageManager;
    private Result savedResultToShow;
    public static final int HISTORY_REQUEST_CODE = 0x0000bacc;
    private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
    private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;
    private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES =
            EnumSet.of(ResultMetadataType.ISSUE_NUMBER,
                    ResultMetadataType.SUGGESTED_PRICE,
                    ResultMetadataType.ERROR_CORRECTION_LEVEL,
                    ResultMetadataType.POSSIBLE_COUNTRY);
    private QRCallBack qrCallBack;
    private boolean isPause = true;
    private SurfaceView surfaceView;

    public MyQRCapture() {
    }

    public MyQRCapture(Activity context) {
        this.context = context;

        Window window = context.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hasSurface = false;
        historyManager = new HistoryManager(context);
        historyManager.trimHistory();
        inactivityTimer = new InactivityTimer(context);
        beepManager = new BeepManager(context);
        ambientLightManager = new AmbientLightManager(context);

        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
    }

    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    public Handler getHandler() {
        return handler;
    }

    public void onResume() {
        try {
            isPause = false;
            if (cameraManager == null)
                cameraManager = new CameraManager(context.getApplication());
            if (viewfinderView == null) {
                viewfinderView = (ViewfinderView) context.findViewById(R.id.viewfinder_view);
                viewfinderView.setCameraManager(cameraManager);

            }

            handler = null;
            lastResult = null;
            resetStatusView();

            if (surfaceView == null)
                surfaceView = (SurfaceView) context.findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
            }
            beepManager.updatePrefs();
            ambientLightManager.start(cameraManager);
            inactivityTimer.onResume();

            Intent intent = context.getIntent();
            source = IntentSource.NONE;
            decodeFormats = null;
            characterSet = null;

            if (intent != null) {
                String action = intent.getAction();
                String dataString = intent.getDataString();
                if (Intents.Scan.ACTION.equals(action)) {
                    source = IntentSource.NATIVE_APP_INTENT;
                    decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
                    decodeHints = DecodeHintManager.parseDecodeHints(intent);
                    if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
                        int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
                        int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
                        if (width > 0 && height > 0) {
                            cameraManager.setManualFramingRect(width, height);
                        }
                    }
                } else if (dataString != null &&
                        dataString.contains("http://www.google") &&
                        dataString.contains("/m/products/scan")) {
                    source = IntentSource.PRODUCT_SEARCH_LINK;
                    sourceUrl = dataString;
                    decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;
                } else if (isZXingURL(dataString)) {
                    source = IntentSource.ZXING_LINK;
                    sourceUrl = dataString;
                    Uri inputUri = Uri.parse(dataString);
                    scanFromWebPageManager = new ScanFromWebPageManager(inputUri);
                    decodeFormats = DecodeFormatManager.parseDecodeFormats(inputUri);
                    decodeHints = DecodeHintManager.parseDecodeHints(inputUri);
                }
                characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        Log.e("", "-------------->myQR onPause<----------------");
        if (isPause)
            return;
        isPause = true;
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) context.findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    public void onDestroy() {
        if (inactivityTimer != null)
            inactivityTimer.shutdown();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i("", "------------>onActivityResult");
        if (resultCode == context.RESULT_OK) {
            if (requestCode == HISTORY_REQUEST_CODE) {
                int itemNumber = intent.getIntExtra(Intents.History.ITEM_NUMBER, -1);
                if (itemNumber >= 0) {
                    HistoryItem historyItem = historyManager.buildHistoryItem(itemNumber);
                    decodeOrStoreSavedBitmap(null, historyItem.getResult());
                }
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        Log.e("", "-------------->initCamera");
        try {
            if (cameraManager != null)
                cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                try {
                    handler = new CaptureActivityHandler(context, decodeFormats, decodeHints,
                            characterSet, cameraManager, viewfinderView, MyQRCapture.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    private static boolean isZXingURL(String dataString) {
        if (dataString == null) {
            return false;
        }
        for (String url : ZXING_URLS) {
            if (dataString.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("", "---------------->surfaceCreated");
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        lastResult = rawResult;

//        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(context, rawResult, MyQRCapture.this);
//
//        boolean fromLiveScan = barcode != null;
//        if (fromLiveScan) {
//            historyManager.addHistoryItem(rawResult, resultHandler);
//            // Then not from history, so beep/vibrate and we have an image to draw on
//            beepManager.playBeepSoundAndVibrate();
//            drawResultPoints(barcode, scaleFactor, rawResult);
//        }
//        switch (source) {
//            case NATIVE_APP_INTENT:
//            case PRODUCT_SEARCH_LINK:
//                handleDecodeExternally(rawResult, resultHandler, barcode);
//                break;
//            case ZXING_LINK:
//                if (scanFromWebPageManager == null || !scanFromWebPageManager.isScanFromWebPage()) {
//                    handleDecodeInternally(rawResult, resultHandler, barcode);
//                } else {
//                    handleDecodeExternally(rawResult, resultHandler, barcode);
//                }
//                break;
//            case NONE:
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//                if (fromLiveScan && prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
//                    restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
//                } else {
//                    handleDecodeInternally(rawResult, resultHandler, barcode);
//                }
//                break;
//        }
        Log.e("", "----------------->rawResult = " + rawResult.getText());
        if (qrCallBack != null)
            qrCallBack.handleDecode(rawResult.getText());
    }

    public void setQRCallBack(QRCallBack qrCallBack) {
        this.qrCallBack = qrCallBack;
    }

    private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(context.getResources().getColor(R.color.result_points));
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4 &&
                    (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
                            rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(),
                    scaleFactor * a.getY(),
                    scaleFactor * b.getX(),
                    scaleFactor * b.getY(),
                    paint);
        }
    }

    private void handleDecodeExternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        if (barcode != null) {
            viewfinderView.drawResultBitmap(barcode);
        }
        long resultDurationMS;
        if (context.getIntent() == null) {
            resultDurationMS = DEFAULT_INTENT_RESULT_DURATION_MS;
        } else {
            resultDurationMS = context.getIntent().getLongExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS,
                    DEFAULT_INTENT_RESULT_DURATION_MS);
        }
        if (source == IntentSource.NATIVE_APP_INTENT) {
            Intent intent = new Intent(context.getIntent().getAction());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
            intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
            byte[] rawBytes = rawResult.getRawBytes();
            if (rawBytes != null && rawBytes.length > 0) {
                intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
            }
            Map<ResultMetadataType, ?> metadata = rawResult.getResultMetadata();
            if (metadata != null) {
                if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
                    intent.putExtra(Intents.Scan.RESULT_UPC_EAN_EXTENSION,
                            metadata.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
                }
                Number orientation = (Number) metadata.get(ResultMetadataType.ORIENTATION);
                if (orientation != null) {
                    intent.putExtra(Intents.Scan.RESULT_ORIENTATION, orientation.intValue());
                }
                String ecLevel = (String) metadata.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
                if (ecLevel != null) {
                    intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, ecLevel);
                }
                @SuppressWarnings("unchecked")
                Iterable<byte[]> byteSegments = (Iterable<byte[]>) metadata.get(ResultMetadataType.BYTE_SEGMENTS);
                if (byteSegments != null) {
                    int i = 0;
                    for (byte[] byteSegment : byteSegments) {
                        intent.putExtra(Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX + i, byteSegment);
                        i++;
                    }
                }
            }
            sendReplyMessage(R.id.return_scan_result, intent, resultDurationMS);
        } else if (source == IntentSource.PRODUCT_SEARCH_LINK) {
            int end = sourceUrl.lastIndexOf("/scan");
            String replyURL = sourceUrl.substring(0, end) + "?q=" + resultHandler.getDisplayContents() + "&source=zxing";
            sendReplyMessage(R.id.launch_product_query, replyURL, resultDurationMS);
        } else if (source == IntentSource.ZXING_LINK) {
            if (scanFromWebPageManager != null && scanFromWebPageManager.isScanFromWebPage()) {
                String replyURL = scanFromWebPageManager.buildReplyURL(rawResult, resultHandler);
                sendReplyMessage(R.id.launch_product_query, replyURL, resultDurationMS);
            }
        }
    }

    private void sendReplyMessage(int id, Object arg, long delayMS) {
        if (handler != null) {
            Message message = Message.obtain(handler, id, arg);
            if (delayMS > 0L) {
                handler.sendMessageDelayed(message, delayMS);
            } else {
                handler.sendMessage(message);
            }
        }
    }

    private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
//        viewfinderView.setVisibility(View.GONE);
        Map<ResultMetadataType, Object> metadata = rawResult.getResultMetadata();
        if (metadata != null) {
            StringBuilder metadataText = new StringBuilder(20);
            for (Map.Entry<ResultMetadataType, Object> entry : metadata.entrySet()) {
                if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
                    metadataText.append(entry.getValue()).append('\n');
                }
            }
            if (metadataText.length() > 0) {
                metadataText.setLength(metadataText.length() - 1);
            }
        }
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public interface QRCallBack {

        public void handleDecode(String strData);
    }

    public void setResult(int resultOk, Intent obj) {

    }

    public void finish() {

    }
}
