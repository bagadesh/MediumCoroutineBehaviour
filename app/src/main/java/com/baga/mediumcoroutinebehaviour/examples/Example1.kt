package com.baga.mediumcoroutinebehaviour.examples

import kotlinx.coroutines.*

fun main(args: Array<String>) {
    Example1().run(CoroutineScope(Dispatchers.IO))
    Thread.sleep(2000)
}


class Example1 {


    fun run(scope: CoroutineScope) {
        val job2ExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("CoroutineExceptionHandler finished $throwable")
        }
        scope.launch(job2ExceptionHandler) {
            println("scope launched")
            val job1 = launch {
                println("job1 launched")
                delay(200)
                println("job1 finished")
            }
            job1.invokeOnCompletion {
                println("job1 invokeOnCompletion")
            }


            val job2 = async {
                println("job2 launched")
                delay(100)
                println("job2 finished")
                throw IllegalAccessException("Test exception")
            }

            try {
                val job3 = launch {
                    println("job3 launched")
                    delay(50)
                    println("job3 finished")
                    throw IllegalAccessException("Test exception job3")
                }

                job3.invokeOnCompletion {
                    println("job3 invokeOnCompletion")
                }
            }catch (e: Exception) {
                println("try catch job3 $e")
            }



            try {
                job2.await()
            }catch (e: Exception) {
                println("Try catch exception $e")
            }




            job2.invokeOnCompletion {
                println("job2 invokeOnCompletion")
            }

            println("scope ended")
        }
    }
}