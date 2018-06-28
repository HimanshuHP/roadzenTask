package com.himanshuph.roadzentask.ui


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.himanshuph.roadzentask.Injection
import com.himanshuph.roadzentask.R
import com.himanshuph.roadzentask.data.model.Question
import com.himanshuph.roadzentask.data.model.RequestDetails
import com.himanshuph.roadzentask.utils.*
import com.himanshuph.roadzentask.utils.rx.AppSchedulerProvider
import kotlinx.android.synthetic.main.fragment_company_detail_request.*
import java.io.IOException


class FormFragment : Fragment(), FormContract.View, DialogCallbackInterface {

    var mPresenter: FormContract.Presenter? = null
    var companyFormHeader: String = ""
    var requesterFormHeader: String = ""
    lateinit var nextBtn: Button
    lateinit var backBtn: Button
    var imageView: ImageView? =null;
    var headerTextView: TextView? = null
    var mCompanyTILInfoList: ArrayList<TextInputInfo> = ArrayList()
    var mRequesterTILInfoList: ArrayList<TextInputInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = FormPresenter(Injection.provideAppDataManager(), AppSchedulerProvider())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_company_detail_request)
        mPresenter?.attachView(this)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter?.getCompanyViewInfo()
    }

    override fun showRequesterFormView(requestDetails: RequestDetails) {
        requesterFormHeader = requestDetails.header
        updateHeaderTextView(requesterFormHeader)
        mRequesterTILInfoList = ArrayList()
        requestDetails.questions.forEach { question ->
            addView(question, mRequesterTILInfoList)
        }

        addBackBtn()
        backBtn.setOnClickListener(backBtnClickListener)
        progressBar.gone()
        errorTv.gone()
        nextBtn.gone()
        updateVisibiltyForCompanyView(View.GONE)
    }

    override fun showCompanyFormView(requestDetails: RequestDetails) {
        companyFormHeader = requestDetails.header
        updateHeaderTextView(companyFormHeader)
        mCompanyTILInfoList = ArrayList()
        requestDetails.questions.forEach { question ->
            addView(question, mCompanyTILInfoList)
        }
        addNextBtn()
        nextBtn.setOnClickListener(nextBtnClickListener)
        progressBar.gone()
        errorTv.gone()
    }


    val nextBtnClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            var isValid = true
            mCompanyTILInfoList.forEach { textInputInfo ->
                val hint = textInputInfo.question.hint
                val validaton = textInputInfo.question.validation
                val til = view?.findViewById<TextInputLayout>(textInputInfo.viewId)
                til?.let {
                    val text = it.getString()
                    if (text.isEmpty()) {
                        it.isErrorEnabled = true
                        it.error = "${hint} cannot be empty"
                        isValid = false
                    } else if (validaton != null && text.length != validaton.size) {
                        it.isErrorEnabled = true
                        it.error = "${hint} must be of length ${validaton.size}"
                        isValid = false
                    } else {
                        it.error = null
                        it.isErrorEnabled = false
                    }
                }
            }

            if (isValid) {
                if (mRequesterTILInfoList.isEmpty())
                    mPresenter?.getRequesterViewInfo()
                else {
                    updateHeaderTextView(requesterFormHeader)
                    updateVisibiltyForRequesterView(View.VISIBLE)
                    updateVisibiltyForCompanyView(View.GONE)
                    nextBtn.gone()
                    backBtn.visible()
                }
            }
        }
    }


    val backBtnClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            var isValid = true
            mRequesterTILInfoList.forEach { textInputInfo ->

                val hint = textInputInfo.question.hint
                val validaton = textInputInfo.question.validation
                val til = if (textInputInfo.question.type.equals("image"))
                    null
                else
                    view?.findViewById<TextInputLayout>(textInputInfo.viewId)
                til?.let {
                    val text = it.getString()
                    if (text.isEmpty()) {
                        it.isErrorEnabled = true
                        it.error = "${hint} cannot be empty"
                        isValid = false
                    } else if (validaton != null && text.length != validaton.size) {
                        it.isErrorEnabled = true
                        it.error = "${hint} must be of length ${validaton.size}"
                        isValid = false
                    } else {
                        it.error = null
                        it.isErrorEnabled = false
                    }
                }
            }

            if (isValid) {
                updateHeaderTextView(companyFormHeader)
                updateVisibiltyForRequesterView(View.GONE)
                updateVisibiltyForCompanyView(View.VISIBLE)
                nextBtn.visible()
                backBtn.gone()
            }
        }
    }

    private fun addNextBtn() {
        nextBtn = Button(context)
        nextBtn.text = "Next"
        nextBtn.setBackgroundColor(Color.BLUE)
        nextBtn.setTextColor(Color.WHITE)
        nextBtn.layoutParams = getLayoutParams().apply { setMargins(50, 100, 50, 0) }
        ll.addView(nextBtn)
    }

    private fun addBackBtn() {
        backBtn = Button(context)
        backBtn.text = "Back"
        backBtn.setBackgroundColor(Color.BLUE)
        backBtn.setTextColor(Color.WHITE)
        backBtn.layoutParams = getLayoutParams().apply { setMargins(50, 100, 50, 50) }
        ll.addView(backBtn)
    }


    private fun addView(question: Question, tilInfoList: ArrayList<TextInputInfo>) {
        when (question.type) {
            "image" -> addImageView(question, tilInfoList)
            "textNumeric" -> addEditText(question, tilInfoList, InputType.TYPE_CLASS_NUMBER)
            else -> addEditText(question, tilInfoList, InputType.TYPE_CLASS_TEXT)
        }
    }

    fun addImageView(question: Question, tilInfoList: ArrayList<TextInputInfo>) {
        try {
            val id = View.generateViewId()
            imageView = ImageView(context)
            imageView?.id = id
            imageView?.layoutParams = getLayoutParams(height = 400).apply { setMargins(20, 100, 20, 50) }
            tilInfoList.add(TextInputInfo(id, question))
            ll.addView(imageView)
            Glide.with(context)
                    .load(question.url)
                    .fitCenter()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView)
            imageView?.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestReadFilePermission()
                    } else
                        choosePhotoFromGallery()
                } else
                    choosePhotoFromGallery()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_READ_REQUEST_CODE)
    }

    override fun onOkClick() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_FILE_REQUEST_CODE)
    }

    override fun onCancelClick() {
        context.toast("Read Permission denied")
    }

    fun requestReadFilePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
            showRationaleDialog(activity, getString(R.string.photo_permission_rationale), this)
        else
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            GALLERY_READ_REQUEST_CODE -> {
                    if (data != null)
                    {
                        val contentURI = data.data
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
                            imageView?.setImageBitmap(bitmap)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            context.toast("Failed!")
                        }

                    }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            READ_FILE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    choosePhotoFromGallery()
                else
                    context.toast("Read Permission denied")
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun addEditText(question: Question, tilInfoList: ArrayList<TextInputInfo>, inputType: Int) {
        val editTextlp = getLayoutParams()
        val tILP = getLayoutParams().apply { setMargins(50, 100, 50, 0) }
        val textInputLayout = TextInputLayout(context)
        textInputLayout.apply {
            id = View.generateViewId()
            tilInfoList.add(TextInputInfo(id, question))
            hint = question.hint
            layoutParams = tILP
        }

        val editText = EditText(context)
        editText.apply {
            id = View.generateViewId()
            this.inputType = inputType
            layoutParams = editTextlp
        }

        textInputLayout.addView(editText, editTextlp)
        ll.addView(textInputLayout, tILP)
    }

    private fun updateHeaderTextView(headerText: String) {
        if (headerTextView != null) {
            headerTextView!!.text = headerText
        } else {
            headerTextView = TextView(context);
            val lp = getLayoutParams()
            lp.setMargins(25, 25, 25, 25);
            headerTextView?.apply {
                text = headerText
                id = View.generateViewId()
                layoutParams = lp
                gravity = Gravity.CENTER;
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f);
            }
            ll.addView(headerTextView)
        }
    }

    fun updateVisibiltyForRequesterView(visibility: Int) {
        mRequesterTILInfoList.forEach { textInputInfo ->
            val til = view?.findViewById<View>(textInputInfo.viewId)
            til?.visibility = visibility
        }
    }

    fun updateVisibiltyForCompanyView(visibility: Int) {
        mCompanyTILInfoList.forEach { textInputInfo ->
            val til = view?.findViewById<View>(textInputInfo.viewId)
            til?.visibility = visibility
        }
    }

    private fun getLayoutParams(width: Int = LinearLayout.LayoutParams.MATCH_PARENT, height: Int = LinearLayout.LayoutParams.WRAP_CONTENT) = LinearLayout.LayoutParams(width, height)

    override fun showLoading() {
        progressBar.visible()
        errorTv.gone()
    }

    override fun showError() {
        progressBar.gone()
        errorTv.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
    }


    companion object {

        val READ_FILE_REQUEST_CODE = 12344

        val GALLERY_READ_REQUEST_CODE = 54663

        @JvmField
        val TAG = FormFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): FormFragment {
            val fragment = FormFragment()
            return fragment
        }
    }
}
