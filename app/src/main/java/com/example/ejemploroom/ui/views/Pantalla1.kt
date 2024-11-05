package com.example.ejemploroom.ui.views

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.ejemploroom.dal.TareaEntity
import com.example.ejemploroom.MainActivity.Companion.basedatos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs


@Composable
fun nuevaTarea (coroutineScope: CoroutineScope,listaTareas : MutableList<TareaEntity>){
    var nombreTarea by remember { mutableStateOf("") }
    Row(
        Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = nombreTarea,
            onValueChange = {nombreTarea = it},
            label = { Text(text = "Añadir tarea") }
        )
        Spacer(Modifier.width(2.dp))
        Button(
            onClick = {
                var task = TareaEntity()
                task.name = nombreTarea
                coroutineScope.launch {
                    basedatos.tareaDao().insert(task)
                    listaTareas.add(task)
                    nombreTarea=""
                }
            }
        ) {
            Text(text = "Añadir")
        }
    }
}

@Composable
fun lista(listaTareas: List<TareaEntity>, coroutineScope: CoroutineScope){
    LazyColumn {
        items(listaTareas){tarea -> vistaTarea(tarea,coroutineScope)}
    }
}

@Composable
fun vistaTarea(tareaEntity: TareaEntity, coroutineScope: CoroutineScope){
    var checked by remember { mutableStateOf(tareaEntity.isDone) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {

                checked = it
                tareaEntity.isDone = checked
                coroutineScope.launch {
                    basedatos.tareaDao().update(tareaEntity)
                }
            }
        )
        Text(
            text = tareaEntity.name
        )
    }
}

@Composable
fun pantalla(){
    val coroutineScope = rememberCoroutineScope()
    var listaTareas = remember { mutableStateListOf<TareaEntity>() }

    LaunchedEffect(Unit) {
        listaTareas.clear()
        listaTareas.addAll(basedatos.tareaDao().getAll())
    }
    Column {
        nuevaTarea(coroutineScope,listaTareas)
        lista(listaTareas, coroutineScope)
    }
}