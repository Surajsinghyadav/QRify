package com.example.qrify


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.core.graphics.toColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

fun generateQRCode(
    content: String,
    size: Int = 512,
    qrBgColor: Color,
    customQrFg: Color,
    customQrBg: Color,
    customBg: Boolean
): Bitmap? {
    val hints = mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",         // Encoding
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H, // High error correction
        EncodeHintType.MARGIN to 1                     // border around QR
    )

    return try {
        val bitMatrix = QRCodeWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            size,
            size,
            hints
        )
        val qrBgColor = qrBgColor.toArgb()
        val customQrFg = customQrFg.toArgb()
        val customQrBg = customQrBg.toArgb()
        val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565)

        for (x in 0 until size) {
            for (y in 0 until size) {
                5
                bitmap[x, y] = if (customBg) {
                    if (bitMatrix[x, y]) customQrBg else customQrFg
                } else {
                    if (bitMatrix[x, y]) qrBgColor else android.graphics.Color.BLACK
                }

            }
        }
        bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
        null

    }


}


@Composable
fun QrGenerator() {
    var textToQr by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var customQrFg by remember { mutableStateOf(Color(0xFF000000)) }
    var customQrBg by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    var qrBgColor by remember { mutableStateOf(Color(0xFDFFFFFF)) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var click by remember { mutableIntStateOf(0) }
    var custmBg by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val saveQR by lazy { SaveQR(context) }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveToGallery(bitmap: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            saveQR.saveImage(bitmap)
        }

    }

    fun getImageUri(content: Context, bitmap: Bitmap): Uri {
        val fileDir = context.cacheDir
        val imagefile = File(fileDir, "image.jpg")

        val outputStream = FileOutputStream(imagefile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return FileProvider.getUriForFile(context, "com.example.qrify.fileprovider", imagefile)
    }



    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .height(338.dp),
            colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
            border = CardDefaults.outlinedCardBorder(true),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, top = 30.dp)
            ) {

                Text(
                    text = "Enter QR Content", Modifier.fillMaxWidth(),
                    fontSize = 21.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Text, URL, Contact Info, Wi-Fi password, or any data.",
                    Modifier.width(250.dp),
                    fontWeight = FontWeight.W300
                )

            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 30.dp, 30.dp, 16.dp)
            ) {
                OutlinedTextField(
                    value = textToQr,
                    onValueChange = { textToQr = it },
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Button(
                    onClick = {

                        when {
                            textToQr.isBlank() -> Toast.makeText(
                                context,
                                "Content is Empty!",
                                Toast.LENGTH_SHORT
                            ).show()

                            else -> {
                                click++
                                qrBitmap = generateQRCode(
                                    textToQr,
                                    qrBgColor = qrBgColor,
                                    customQrFg = customQrFg,
                                    customQrBg = customQrBg,
                                    customBg = custmBg
                                )
                            }
                        }

                        keyboardController?.hide()
//                        Toast.makeText(context, textToQr, Toast.LENGTH_SHORT).show()
                    },
                    Modifier.padding(start = 50.dp, end = 50.dp, top = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.QrCode2, contentDescription = "Qr icon")
                        Text(
                            "Generate",
                            Modifier.padding(start = 18.dp, top = 2.dp)
                        )
                    }
                }


            }

        }
        Spacer(Modifier.height(12.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .height(400.dp),
            colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
            border = CardDefaults.outlinedCardBorder(true),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier) {
                        Text(
                            "Generated QR Code", Modifier.padding(0.dp),
                            fontSize = 18.sp
                        )
                        Text(
                            "Scan this code to verify content.", modifier = Modifier,
                            fontWeight = FontWeight.W300
                        )
                    }
                    IconButton(onClick = {
                        if (qrBitmap != null) {
                            val imageUri = getImageUri(context, qrBitmap!!)
                            if (imageUri != null) {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    putExtra(Intent.EXTRA_STREAM, imageUri)
                                    type = "image/jpeg"
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share QR Code"))

                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to generate sharable QR",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Toast.makeText(context, "No QR Found", Toast.LENGTH_SHORT).show()
                        }

                    }) {
                        Icon(
                            Icons.Default.IosShare, contentDescription = null, Modifier.size(25.dp)
                        )

                    }
                    IconButton(onClick = {
                        if (qrBitmap != null) {
                            saveToGallery(qrBitmap!!)
                        } else {
                            Toast.makeText(context, "No QR Found", Toast.LENGTH_SHORT).show()
                        }
                    }) {

                        Icon(
                            Icons.Default.Download,
                            contentDescription = null,
                            Modifier.size(25.dp)
                        )

                    }

                }



                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
//                    if (qrBitmap == null && click >= 1) {
//                        Text("Content too long to be generated!", color = Color.Red)
//                    }
                    when {
                        qrBitmap == null && click < 1 -> DemoQr()
                        qrBitmap == null && click >= 1 -> {
                            Box(Modifier.size(300.dp)) {
                                Column(
                                    Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Content too long to be generated!",
                                        color = Color.Red,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 6.dp)
                                            .wrapContentSize(Alignment.Center)
                                    )
                                    val context = LocalContext.current
                                    val video = R.raw.scanfailed

                                    val demoVideo = Animated_Video()
                                    demoVideo.Play(video, context)
                                }
                            }
                        }

                        else -> Image(
                            bitmap = qrBitmap!!.asImageBitmap(),
                            contentDescription = "Generated QR Code",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        )
                    }

                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .height(250.dp),
            colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
            border = CardDefaults.outlinedCardBorder(true),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Customize Appearance", Modifier.padding(0.dp),
                    fontSize = 18.sp
                )
                Text(
                    "Choose colors for your QR code.", modifier = Modifier,
                    fontWeight = FontWeight.W300
                )
                Text(
                    "Colors", Modifier
                        .align(Alignment.Start)
                        .padding(top = 16.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )


                LazyRow(
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {

                    item {
                        Card(
                            onClick = {
                                custmBg = true
                                showDialog = true

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(100.dp)
                                .padding(4.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                width = if (custmBg) 2.dp else 1.dp,
                                color = if (custmBg) Color.Black else Color.Gray
                            )
                        ) {
                            Text(
                                "Custom\n  Color", Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(Alignment.Center),
                                fontSize = 12.sp
                            )

                        }
                    }

                    itemsIndexed(ColorsCollection) { index, col ->

                        Card(
                            onClick = {
                                selectedIndex = index
                                qrBgColor = Color("#${col.ColorCode}".toColorInt())
                                custmBg = false

                                if (textToQr.isNotBlank()) {
                                    qrBitmap = generateQRCode(
                                        textToQr,
                                        qrBgColor = qrBgColor,
                                        customQrFg = customQrFg,
                                        customQrBg = customQrBg,
                                        customBg = custmBg
                                    )
                                }

                            },

                            Modifier
                                .size(100.dp)
                                .padding(4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(Color("#${col.ColorCode}".toColorInt())),
                            border = BorderStroke(
                                width = if (selectedIndex == index && !custmBg) 2.dp else 1.dp,
                                if (selectedIndex == index && !custmBg) Color.Black else Color.Gray
                            )
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.QrCode2,
                                    contentDescription = "Qr sample",
                                    Modifier
                                        .padding(top = 0.dp)
                                        .size(70.dp)
                                        .align(Alignment.CenterHorizontally),
                                )
                                Text(
                                    col.ColorName, Modifier,
                                    fontSize = 12.sp,
                                )

                            }

                        }
                    }
                }
                Spacer(Modifier.height(20.dp))

            }
        }

        if (showDialog) {
            CustomColorInput(onDismiss = { showDialog = false }, onConfirm = { fg, bg ->
                customQrFg = if (fg.isNullOrEmpty()) customQrFg else Color("#${fg}".toColorInt())
                customQrBg = if (bg.isNullOrEmpty()) customQrBg else Color("#${bg}".toColorInt())
                if (textToQr.isNotBlank()) {
                    qrBitmap = generateQRCode(
                        textToQr,
                        qrBgColor = qrBgColor,
                        customQrFg = customQrFg,
                        customQrBg = customQrBg,
                        customBg = custmBg
                    )
                }
                showDialog = false
            })
        }


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomColorInput(onDismiss: () -> Unit, onConfirm: (String?, String?) -> Unit) {
    var fgColor by remember { mutableStateOf("") }
    var bgColor by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun isValidHexColor(input: String): Boolean {
        val regex = Regex("^#?[0-9A-Fa-f]{6}$")
        return regex.matches(input)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Custom Color") },
        text = {
            Column {
                OutlinedTextField(
                    value = fgColor,
                    onValueChange = { fgColor = it },
                    label = { Text("Foreground Color") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = fgColor.isNotEmpty() && !isValidHexColor(fgColor),
                    suffix = {
                        if (isValidHexColor(fgColor)) {
                            Icon(
                                Icons.Default.Circle,
                                contentDescription = null,
                                tint = Color("#${fgColor.trimStart('#')}".toColorInt())
                            )

                        }
                    }
                )

                OutlinedTextField(
                    value = bgColor,
                    onValueChange = { bgColor = it },
                    label = { Text("Background Color") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = bgColor.isNotEmpty() && !isValidHexColor(bgColor),
                    suffix = {
                        if (isValidHexColor(bgColor)) {
                            Icon(
                                Icons.Default.Circle,
                                contentDescription = null,
                                tint = Color("#${bgColor.trimStart('#')}".toColorInt())
                            )

                        }
                    }
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 12.sp
                    )

                }

            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (isValidHexColor(fgColor) && isValidHexColor(bgColor)) {
                    onConfirm(fgColor.trimStart('#'), bgColor.trimStart('#'))
                    onDismiss()
                } else {
                    errorMessage = "Please enter valid hex colors like #A0C878"

                }
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text("Cancel")
            }
        }

    )


}


@Composable
fun DemoQr() {
    Box(Modifier.size(300.dp)) {
        val context = LocalContext.current
        val video = R.raw.qrmotion

        val demoVideo = Animated_Video()
        demoVideo.Play(video, context)
    }
}

