
package com.smartkitchen.ui

import android.Manifest
import android.content.pm.PackageManager
import android.app.Activity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.viewinterop.AndroidView
import com.smartkitchen.model.Category
import com.smartkitchen.model.Ingredient
import com.smartkitchen.viewmodel.IngredientsViewModel
import com.smartkitchen.viewmodel.ViewModelFactory

@Composable
fun BarcodeScannerScreen(
    navController: NavController,
    viewModel: IngredientsViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var barcodeValue by remember { mutableStateOf<String?>(null) }

    Column(Modifier.padding(16.dp)) {
        Text("Barcode Scanner", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Scanned Value: ${'$'}{barcodeValue ?: \"Waiting for scan...\"}")
        Spacer(modifier = Modifier.height(20.dp))

        AndroidView(factory = { ctx ->
            PreviewView(ctx).apply {
                if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ctx as Activity, arrayOf(Manifest.permission.CAMERA), 0)
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(surfaceProvider)
                    }

                    val scanner: BarcodeScanner = BarcodeScanning.getClient()
                    val analysis = ImageAnalysis.Builder().build().also { ia ->
                        ia.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { proxy: ImageProxy ->
                            val mediaImage = proxy.image
                            if (mediaImage != null) {
                                val image = InputImage.fromMediaImage(mediaImage, proxy.imageInfo.rotationDegrees)
                                scanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        if (barcodes.isNotEmpty() && barcodeValue == null) {
                                            val code = barcodes.first().rawValue
                                            barcodeValue = code
                                            if (code != null) {
                                                viewModel.addIngredient(
                                                    Ingredient(
                                                        name = code,
                                                        quantity = 1,
                                                        unit = "pieces",
                                                        category = Category.VEGETABLES.name
                                                    )
                                                )
                                                navController.navigate("ingredients")
                                            }
                                        }
                                    }
                                    .addOnCompleteListener { proxy.close() }
                            } else {
                                proxy.close()
                            }
                        }
                    }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
                }, ContextCompat.getMainExecutor(ctx))
            }
        })

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate("ingredients") }) {
            Text("Back to Ingredients")
        }
    }
}
