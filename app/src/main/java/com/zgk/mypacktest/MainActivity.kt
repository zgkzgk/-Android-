package com.zgk.mypacktest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved", 0)
        val m=MainViewModelFactory(countReserved)
        val userDao=AppDatabase.getDatabase(this).userDao()
        val user1=User("Tom","Brady",11)
        val user2=User("Tom","Hanks",63)
        addDataBtn.setOnClickListener {
            thread {
                user1.id=userDao.insertUser(user1)
                user2.id=userDao.insertUser(user2)
            }
        }
        updateDataBtn.setOnClickListener {
            thread {
                user1.age=42
                userDao.updateUser(user1)
            }
        }
        deleteDataBtn.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("Hanks")
            }
        }
        queryDataBtn.setOnClickListener {
            thread {
                for (user in userDao.loadAllUsers()){
                    Log.d("MainActivity",user.toString())
                }
            }
        }
        viewModel = ViewModelProviders.of(this,m).get(MainViewModel::class.java)
        plusOneBtn.setOnClickListener {
            viewModel.plusOne()
        }
        clearBtn.setOnClickListener {
            viewModel.clear()
        }
        getUserBtn.setOnClickListener {
            val userId = (0..10000).random().toString()
            viewModel.getUser(userId)
        }
        viewModel.user.observe(this, Observer { user->
            infoText.text=user.firstName
        })
        viewModel.counter.observe(this, Observer { count->
            infoText.text=count.toString()
        })
    }

    override fun onPause() {
        super.onPause()
        sp.edit{
            putInt("count_reserved", viewModel.counter.value ?:0)
        }
    }
}