package com.ziven0069.asesment1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ziven0069.asesment1.R
import com.ziven0069.asesment1.navigation.Screen
import com.ziven0069.asesment1.ui.theme.Asesment1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var berat by rememberSaveable { mutableStateOf("") }
    var beratError by rememberSaveable { mutableStateOf(false) }
    var selectKategori by remember { mutableStateOf<String?>(null) }
    var air by rememberSaveable { mutableFloatStateOf(0f) }
    var dataVerif by remember { mutableStateOf("") }
    var peringatan by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = stringResource(id = R.string.air_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = berat,
            onValueChange = { berat = it
                selectKategori=null
                            air = 0f
                            dataVerif = ""},
            label = { Text(text = stringResource(R.string.berat_badan)) },
            trailingIcon = { IconPicker(beratError ,"kg") },
            supportingText = { ErrorHint(beratError) },
            isError = beratError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))

        ) {
            RadioOption(
                label = stringResource(R.string.anakanak),
                imageRes = R.drawable.boy_happy_childhood_icon_isolated,
                selected = selectKategori == "anakanak",
                onClick = {selectKategori="anakanak"
                dataVerif = ""}
            )
            RadioOption(
                label = stringResource(R.string.dewasa),
                imageRes = R.drawable._pngtree_hand_drawn_cartoon_father_s_day_5617131_removebg_preview,
                selected = selectKategori == "dewasa",
                onClick = {selectKategori="dewasa"
                dataVerif = ""}
            )
        }
        Button(
            onClick = {
                val beratFloat = berat.toFloatOrNull()
                beratError = (beratFloat == null || beratFloat <= 0f)
                if (beratError) return@Button
                when (selectKategori) {
                    "anakanak" -> {
                        if (berat.toFloat() > 40){
                            dataVerif = "Salah"
                            peringatan = peringatanAnak(context)
                        } else {
                            dataVerif = "Benar"
                            air = hitungAiranak(berat.toFloat())
                        }
                    }

                    "dewasa" -> {
                        if (berat.toFloat() > 40){
                            dataVerif = "Benar"
                            air = hitungDewasa(berat.toFloat())
                        } else {
                            dataVerif = "Salah"
                            peringatan = peringatanDewasa(context)
                        }
                    }
                }            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.hitung))
        }
        if (air != 0f && dataVerif == "Benar") {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )

            Text(
                text = stringResource(R.string.air_ml, air),
                style = MaterialTheme.typography.titleLarge
            )

            val kategoriText = when (selectKategori) {
                "anakanak" -> context.getString(R.string.anakanak)
                "dewasa" -> context.getString(R.string.dewasa)
                else -> "-"
            }

            Text(
                text = kategoriText,
                style = MaterialTheme.typography.headlineLarge
            )

            Button(
                onClick = {
                    shareData(
                        context = context,
                        message = context.getString(
                            R.string.share_template,
                            berat,
                            kategoriText,
                            air,
                        )
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.share))
            }
        } else if (air != 0f && dataVerif == "Salah"){
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(
                text = peringatan,
                style = MaterialTheme.typography.titleLarge
            )
        }else{
            Text(
                text = "",
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}

private fun hitungAiranak(berat: Float): Float {
    val hasil: Float
    val a: Float
    if (berat < 10) {
        hasil = berat * 100
    } else if (berat < 20 && berat > 10) {
        hasil = ((berat - 10) * 50) + 1000
    } else {
        a = berat * 30
        hasil = ((((berat - 20) * 20) + 1500) + a) / 2
    }
    return hasil
}

private fun hitungDewasa(berat: Float): Float {
    val hasil = berat * 30
    return hasil
}

fun peringatanAnak(context: Context): String {
    return context.getString(R.string.peringatanAnak)
}

fun peringatanDewasa(context: Context): String {
    return context.getString(R.string.peringatanDewasa)
}

private fun shareData(context: Context, message:String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}
@Composable
fun RadioOption(
    label: String,
    imageRes: Int,
    selected: Boolean,
    onClick:()->Unit
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.padding(8.dp)
    ){
        Image(
            painter= painterResource(id=imageRes),
            contentDescription=label,
            modifier = Modifier.size(80.dp).border(
                width = if(selected){3.dp} else 1.dp,
                color = if(selected)Color.Blue else Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )
        )
        Text(text = label)
    }
}
@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    }else{
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input_invalid))
    }
}
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asesment1Theme {
        MainScreen(rememberNavController())
    }
}