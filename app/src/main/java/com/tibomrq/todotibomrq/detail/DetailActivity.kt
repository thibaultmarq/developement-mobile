package com.tibomrq.todotibomrq.detail

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tibomrq.todotibomrq.detail.ui.theme.TodotibomrqTheme
import com.tibomrq.todotibomrq.list.Task

import java.util.UUID

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        var task = intent?.getSerializableExtra(Task.TASK_KEY) as Task?
        enableEdgeToEdge()
        setContent {
            TodotibomrqTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail (
                        modifier = Modifier.padding(innerPadding),
                        fun(task){
                            intent.putExtra(Task.TASK_KEY, task )
                            setResult(RESULT_OK, intent)
                            finish()
                        }, task
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Detail ( modifier: Modifier = Modifier, onValidate: (Task) -> Unit, initialTask: Task?) {
    val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task")
    var task by remember { mutableStateOf(initialTask ?: newTask)}

    Column(Modifier.padding(16.dp), verticalArrangement =  Arrangement.spacedBy(16.dp)) {
        Text(

            text = "Task Detail",
            modifier = modifier,
        )
        OutlinedTextField(
            value = task.title,
            onValueChange = {task = task.copy(title = it)},
            modifier = modifier,
        )

        OutlinedTextField(
            value = task.description,
            onValueChange = {task = task.copy(description = it)},
            modifier = modifier,
        )

        Button(
            onClick = {
                //val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !")

                onValidate(task)
                      },
            modifier = modifier,
            enabled = true,
            shape = RectangleShape,
            colors = ButtonColors(Color.Black,Color.Red,Color.Blue,Color.Green),
        ) { }
    }
}




@Preview(showBackground = true)
@Composable
fun DetailPreview () {
    val task : Task
    TodotibomrqTheme {
        //Detail (modifier = Modifier,fun(task){})
    }
}