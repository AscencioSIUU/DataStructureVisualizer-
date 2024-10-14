package com.example.datastructurevisualizerapp.components.homeComponents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.datastructurevisualizerapp.R

@Composable
fun dataStructureBox(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    //se usa string resource porque viene de un stringRes, asi fue configurado en el home
    val textString = stringResource(text)
    Box(
        modifier = modifier
            .size(220.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = colorResource(R.color.cardBackground))
            .clickable { navController.navigate(textString)},
        contentAlignment = Alignment.Center,

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 8.dp)
            )
            Text(
                text = textString,
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.textCardColor),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
/**
@Preview
@Composable
fun previewDataStructureBox() {
    dataStructureBox(
        modifier = Modifier,
        imageRes = R.drawable.binarytreesicon,
        title = "Binary Tree"
    )
}
*/