package com.example.localbookfilter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.localbookfilter.data.Author.Author
import com.example.localbookfilter.data.Author.AuthorViewModel
import com.example.localbookfilter.data.Book.Book
import com.example.localbookfilter.data.Book.BookViewModel


class MainActivity : AppCompatActivity() {

    var authorName = ""

    private lateinit var mAuthorViewModel: AuthorViewModel
    private lateinit var mBookViewModel: BookViewModel

    private lateinit var httpApiService: HttpApiService
    private lateinit var myApp: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuthorViewModel = ViewModelProvider(this).get(AuthorViewModel::class.java)
        mBookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        myApp = application as MyApplication
        httpApiService = myApp.httpApiService


//just trying out something->

//        txtAuthor.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                txtAuthor.hint = ""
//                //Do something when EditText has focus
//            } else {
//                txtAuthor.hint = "Enter author name..."
//                // Do something when Focus is not on the EditText
//            }
//        })


    }

    override fun onStart() {
        super.onStart()

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        val firstTimeUse = sharedPref.getBoolean("first_time", true)

        if (firstTimeUse) {

            Toast.makeText(this, "Downloading data...", Toast.LENGTH_LONG).show()
            insertAuthorDataToDb()
            insertBookDataToDb() // here the app crashes

            //just for toast message to display properly
            Thread.sleep(2000)

            Toast.makeText(this, "Download Complete!", Toast.LENGTH_LONG).show()

            val editor = sharedPref.edit()
            editor.putBoolean("first_time", false)
            editor.apply()

        }

        filterBooksDisplay()

    }

    private fun filterBooksDisplay() {
        val txtAuthor = findViewById<TextInputEditText>(R.id.txtAuthor)
        val btnFilter = findViewById<Button>(R.id.btnFilter)

        val tvResultCount = findViewById<TextView>(R.id.tvResultCount)
        val tvResult1 = findViewById<TextView>(R.id.tvResult1)
        val tvResult2 = findViewById<TextView>(R.id.tvResult2)
        val tvResult3 = findViewById<TextView>(R.id.tvResult3)

        btnFilter.setOnClickListener {

            authorName = txtAuthor.text.toString()

            if (authorName.isNullOrEmpty()) {
                tvResultCount.text = "please fill out field"
                tvResult1.text = ""
                tvResult2.text = ""
                tvResult3.text = ""
            } else {
                mBookViewModel.readData(authorName)
                    .observe(this@MainActivity, Observer { books ->

                        var count = books.size

                        //find top 3 results
                        tvResultCount.text = "Results: $count"

                        if (count >= 3) {
                            val name1 = books.get(0).title
                            val id1 = books.get(0).book_id
                            tvResult1.text = "Result: $name1 ($id1)"

                            val name2 = books.get(1).title
                            val id2 = books.get(1).book_id
                            tvResult2.text = "Result: $name2 ($id2)"

                            val name3 = books.get(2).title
                            val id3 = books.get(2).book_id
                            tvResult3.text = "Result: $name3 ($id3)"
                        }

                        if (count == 2) {
                            val name1 = books.get(0).title
                            val id1 = books.get(0).book_id
                            tvResult1.text = "Result: $name1 ($id1)"

                            val name2 = books.get(1).title
                            val id2 = books.get(1).book_id
                            tvResult2.text = "Result: $name2 ($id2)"

                            tvResult3.text = ""
                        }

                        if (count == 1) {
                            val name1 = books.get(0).title
                            val id1 = books.get(0).book_id
                            tvResult1.text = "Result: $name1 ($id1)"

                            tvResult2.text = ""
                            tvResult3.text = ""
                        }

                        if (count == 0) {
                            tvResultCount.text = "nothing found!"
                            tvResult1.text = ""
                            tvResult2.text = ""
                            tvResult3.text = ""
                        }

                    })

            }


        }


    }


    private fun insertBookDataToDb() {

        mBookViewModel.insertData(mAuthorViewModel, httpApiService)
    }


    private fun insertAuthorDataToDb() {

        mAuthorViewModel.insertData(mAuthorViewModel, httpApiService)
    }


}