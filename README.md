# AdvanceQRScanner
 Android library that helps the developers to implements the QR Scanner with heighly customization.

## Installation
  Add it in your root build.gradle at the end of repositories:
```bash
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```bash
dependencies {
	       implementation 'com.github.RufenKhokhar:AdvanceQRScanner:1.0'
	}
```
## Useage
  xml code
```xml
<com.rkgroup.advanceqrscanner.ScannerView
        android:id="@+id/scannerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:enableLaser="true|false"
        app:frameAspectRatioHeight="0.1 to 1.0"
        app:frameAspectRatioWidth="0.1 to 1.0"
        app:frameColor="@android:color/white"
        app:frameCornersRadius="36dp"
        app:frameCornersSize="36dp"
        app:frameFixedSize="true|false"
        app:frameScalarImage="@drawable/..."
        app:frameScalarImageColor="@android:color/white"
        app:frameSize="0.1 to 1.0"
        app:frameThickness="8dp"
        app:insideFrameColor="@color/black"
        app:insideFrameColorEnable="true|false"
        app:laserColor="@android:color/red"
        app:maskColor="#54090909" />
```
## JAVA code
```java

public class QRActivity extends AppCompatActivity {

    private Scanner scanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScannerView scannerView = findViewById(R.id.scannerView);
        scanner = new Scanner(this, scannerView);
        scannerView.setmIsLaserEnabled();
        scannerView.setLaserColor();
        scannerView.setLaserColor();
        scannerView.setFrameScalarImage();
        scannerView.setFrameScalarImageColor();
        scannerView.setFrameFixed();
        scannerView.setFrameColor();
        scannerView.setFrameCornersRadius();
        scannerView.setFrameCornersSize();
        scannerView.setFrameAspectRatio();
        scannerView.setFrameAspectRatioHeight();
        scannerView.setFrameAspectRatioWidth();
        scannerView.setFrameThickness();
        scannerView.setFrameSize();
        scannerView.setMaskColor();
        scannerView.setInsideFrameColor();// frame inside color is show when frame scaling
        // scanner object methods
        scanner.setAutoFocusEnabled();
        scanner.setTouchFocusEnabled();
        scanner.setAutoFocusInterval();
        scanner.setDecodeCallback(result -> {
        });
        scanner.setFlashEnabled();
        scanner.setScanMode();
        scanner.setErrorCallback(error -> {
        });
        scanner.getMaxZoom();
        scanner.setZoom();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        scanner.onResume();
    }

    @Override
    protected void onPause() {
        scanner.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        scanner.onDestroy();
        super.onDestroy();
    }
}
```
## Kotlin code
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var scanner: Scanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val scannerView = findViewById<ScannerView>(R.id.scannerView)
        scanner = Scanner(this, scannerView)
        scannerView.setmIsLaserEnabled()
        scannerView.setLaserColor()
        scannerView.setLaserColor()
        scannerView.setFrameScalarImage()
        scannerView.setFrameScalarImageColor()
        scannerView.setFrameFixed()
        scannerView.setFrameColor()
        scannerView.setFrameCornersRadius()
        scannerView.setFrameCornersSize()
        scannerView.setFrameAspectRatio()
        scannerView.setFrameAspectRatioHeight()
        scannerView.setFrameAspectRatioWidth()
        scannerView.setFrameThickness()
        scannerView.setFrameSize()
        scannerView.setMaskColor()
        scannerView.setInsideFrameColor() // frame inside color is show when frame scaling

        // scanner object methods
        scanner.setAutoFocusEnabled()
        scanner.setTouchFocusEnabled()
        scanner.setAutoFocusInterval()
        scanner.decodeCallback = DecodeCallback { result: Result? -> }
        scanner.setFlashEnabled()
        scanner.setScanMode()
        scanner.errorCallback = ErrorCallback { error: Exception? -> }
        scanner.maxZoom
        scanner.setZoom()

    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        scanner.onResume()
    }

    override fun onPause() {
        scanner.onPause()
        super.onPause()

    }

    override fun onDestroy() {
        scanner.onDestroy()
        super.onDestroy()
    }

}

```

## Note:
  if you want to use these properties you need to enable the ``` ScannerView.setFrameFixed(true) ```
  

        scannerView.setFrameAspectRatio()
        scannerView.setFrameAspectRatioHeight()
        scannerView.setFrameAspectRatioWidth()
        scannerView.setFrameSize()
       
 
 ## This Libray inspired by the [yuriy-budiyev/code-scanner](https://github.com/yuriy-budiyev/code-scanner)


