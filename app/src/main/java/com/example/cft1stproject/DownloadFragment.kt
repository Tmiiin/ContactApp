package com.example.cft1stproject

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cft1stproject.model.ContactView
import com.example.cft1stproject.model.json.PostImgModel
import com.example.cft1stproject.retrofit.ImgurAPI
import kotlinx.android.synthetic.main.download_to_imgur.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DownloadFragment(val mContext: Context, private val contactView: ContactView) : Fragment(),
    OnBackPressedListener {

    companion object {
        fun newInstance(mContext: Context, contactView: ContactView) =
            DownloadFragment(mContext, contactView)

        private const val TAG = "DownloadFragment"
        private const val text = "Ошибка"
        private const val plaintext = "text/plain"
        private const val baseUrl = "https://api.imgur.com/3/"
        private const val tokenType = "Bearer "
        private const val token = "4e78271141fc5484d977990a46b891cb765e015c"
        private const val fileType = "url"

        lateinit var retrofit: Retrofit
        lateinit var mService: ImgurAPI

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.download_to_imgur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageURI = Uri.parse(contactView.imageUri)
        download_image.setImageURI(imageURI)
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mService = retrofit.create(ImgurAPI::class.java)
        download_button.setOnClickListener {
            GlobalScope.launch {
                downloadToImgur(imageURI)
            }
        }
    }

    suspend fun downloadToImgur(imageUri: Uri) {
        try {
            //  val file = File(imageUri.path)
            /*  val fbody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
              val type: RequestBody =
                  RequestBody.create(MediaType.parse("text/plain"), "file")*/

             */
            val file: RequestBody =
                RequestBody.create(
                    MediaType.parse(plaintext),
                    "https://sun9-65.userapi.com/TvtBrhIRBny_FUteO5-ytnRnf2UYa7jUbP9dfg/5r7qn9toZ58.jpg"
                )
            val type: RequestBody =
                RequestBody.create(MediaType.parse(plaintext), fileType)
            val title: RequestBody =
                RequestBody.create(MediaType.parse(plaintext), download_edit_title.text.toString())
            val description: RequestBody =
                RequestBody.create(MediaType.parse(plaintext), download_edit_desc.text.toString())
            val call = mService.uploadImg(
                tokenType + token,
                type = type, title = title, description = description, file = file
            )
            call.enqueue(object : Callback<PostImgModel> {
                override fun onResponse(
                    call: Call<PostImgModel>,
                    response: Response<PostImgModel>
                ) {
                    if (response.isSuccessful) {
                        val response = response.body()!!
                        Log.i(TAG, response.data.toString())
                    } else Log.i(TAG, response.code().toString())
                }

                override fun onFailure(call: Call<PostImgModel>, t: Throwable) {
                    Log.i(TAG, t.message)
                }
            })
        } catch (e: Exception) {
            Log.i(TAG, e.message)
        }
    }

    override fun onBackPressed() {
        childFragmentManager.popBackStack()
    }


}