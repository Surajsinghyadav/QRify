package com.example.qrify

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

fun launchQRScanner(context: Context, onScanned: (String) -> Unit) {
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .enableAutoZoom()
        .build()

    val scanner = GmsBarcodeScanning.getClient(context, options)
    val moduleInstall = ModuleInstall.getClient(context)

    // Check if modules are available
    moduleInstall.areModulesAvailable(scanner)
        .addOnSuccessListener { response ->
            if (response.areModulesAvailable()) {
                // Modules are available, start scanning
                startScanning(scanner, context, onScanned)
            } else {
                // Install required modules
                val request = ModuleInstallRequest.newBuilder()
                    .addApi(scanner)
                    .build()

                moduleInstall.installModules(request)
                    .addOnSuccessListener {
                        // Module installation succeeded
                        Handler(Looper.getMainLooper()).postDelayed({
                            startScanning(scanner, context, onScanned)
                        }, 2000) // Increased delay to ensure module is ready
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Module installation failed: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(
                context,
                "Failed to check module availability: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
}

private fun startScanning(
    scanner: com.google.mlkit.vision.codescanner.GmsBarcodeScanner,
    context: Context,
    onScanned: (String) -> Unit
) {
    scanner.startScan()
        .addOnSuccessListener { barcode ->
            barcode.rawValue?.let {
                onScanned(it)
                Toast.makeText(context, "QR Code scanned successfully!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "QR Code data is empty", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnCanceledListener {
            Toast.makeText(context, "Scan cancelled", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Scan failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
}


@Composable
fun Scanner() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf<String>("Scan", " Generate")
    val context = LocalContext.current
    var scanResult by remember { mutableStateOf("") }
    val Scrollstate = rememberScrollState()

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 54.dp)
                .background(Color.White)
                .verticalScroll(Scrollstate),
            horizontalAlignment = Alignment.CenterHorizontally,


            ) {
            Text(
                when (selectedIndex) {
                    0 -> "Scan QR Code"
                    else -> "Generate QR Code"

                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 24.sp,
                fontWeight = FontWeight.W500
            )



            SingleChoiceSegmentedButtonRow(
                Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                options.forEachIndexed { index, option ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size,
                            baseShape = RoundedCornerShape(10.dp)
                        ),

                        modifier = Modifier.width(368.dp),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color(0xFF8BC34A),
                            inactiveContainerColor = Color.Transparent
                        ),
                        border = BorderStroke(1.dp, Color(0xffDEE1E6)),
                        onClick = {
                            selectedIndex = index
                        },
                        selected = selectedIndex == index,
                        label = {
                            Text(
                                option,
                                fontSize = 16.sp
                            )
                        }
                    )

                }
            }


            when (selectedIndex) {
                0 -> ScanCard(scanResult, context)
                else -> QrGenerator()
            }


        }
        /* box end */
    }
}

@Composable
fun ScanCard(scanResult: String, context: Context) {
    val Scrollstate = rememberScrollState()
    var scanResult by remember { mutableStateOf(scanResult) }
    var isLink by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val scanHistory = remember { ScanHistory() }
    var recentlyScanned by remember { mutableStateOf(scanHistory.addToScans(scanResult, context)) }

    LaunchedEffect(Unit) {
        recentlyScanned = scanHistory.addToScans("", context)
    }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 8.dp,
            focusedElevation = 8.dp,
            hoveredElevation = 8.dp,
            draggedElevation = 8.dp,
            disabledElevation = 8.dp,
        ),
        border = CardDefaults.outlinedCardBorder(true),
        shape = RoundedCornerShape(16.dp),

        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable(enabled = true, onClickLabel = null, onClick = {
                    launchQRScanner(context) { onScanned ->
                        scanResult = onScanned
                        isLink =
                            scanResult.startsWith("https://") or scanResult.startsWith("http://")
                        recentlyScanned = scanHistory.addToScans(scanResult, context)
                    }
                }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(Modifier
                .size(150.dp)
                .padding(start = 6.dp)) {
                val context = LocalContext.current
                val video = R.raw.qrfirst

                val demoVideo = Animated_Video()
                demoVideo.Play(video, context)
            }
            Text(
                text = "Click to scan",
                fontWeight = FontWeight.W300,
                modifier = Modifier
            )
        }

    }

    Card(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp, top = 4.dp, end = 16.dp),
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column {
            Text(
                text = "Scan Result",
                Modifier
                    .align(Alignment.Start)
                    .padding(8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.W400,
                fontStyle = FontStyle.Italic
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                color = Color.Gray
            )
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (scanResult.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,

                            )
                        Text(text = "Scan to see results")
                    }
                } else {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .verticalScroll(Scrollstate)
                    ) {
                        Text(
                            text = scanResult,
                            fontSize = 16.sp
                        )

                    }
                }

            }


        }
    }
    if (scanResult.isNotEmpty()) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(scanResult))
                            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()

                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(Modifier, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null)
                            Spacer(Modifier.padding(4.dp))
                            Text(
                                text = "Copy", Modifier.padding(top = 1.dp),
                                fontSize = 16.sp
                            )
                        }


                    }
                    Spacer(Modifier.width(12.dp))
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, scanResult)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(intent, null)
                        context.startActivity(shareIntent)

                    }, shape = RoundedCornerShape(12.dp)) {
                        Row(Modifier, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.Share, contentDescription = null)
                            Spacer(Modifier.padding(4.dp))
                            Text(
                                text = "Share", Modifier.padding(top = 1.dp),
                                fontSize = 16.sp
                            )
                        }


                    }
                }

                Button(
                    onClick = {
                        if (isLink) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scanResult))
                            context.startActivity(intent)

                        }

                    },
                    enabled = isLink,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF8BC34A))
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                    Spacer(Modifier.padding(4.dp))
                    Text(
                        text = "Open", Modifier.padding(top = 1.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    Column(Modifier.fillMaxWidth()) {
        if (recentlyScanned.isNotEmpty()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Recent Scans", Modifier.padding(start = 16.dp, top = 16.dp), fontSize = 18.sp)
                TextButton(onClick = {
                    recentlyScanned = scanHistory.addToScans("", context, true)
                }) {
                    Text("Delete all")
                }
            }
        }

        LazyRow(Modifier.fillMaxWidth()) {

            items(recentlyScanned.toList().reversed()) { (key, values) ->
                if (values.toString().isNotBlank()) {
                    Card(onClick = {
                        scanResult = values.toString()
                        isLink =
                            scanResult.startsWith("https://") or scanResult.startsWith("http://")


                    }, Modifier
                        .size(150.dp)
                        .padding(16.dp)) {
//                        Text(key.toString() , Modifier.fillMaxSize().padding(4.dp).wrapContentSize(Alignment.Center))
                        Text(
                            values.toString(),
                            Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .wrapContentSize(Alignment.Center)
                        )
                    }

                }


            }
        }

    }


}

