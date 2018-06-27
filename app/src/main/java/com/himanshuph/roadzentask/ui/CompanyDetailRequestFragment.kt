package com.himanshuph.roadzentask.ui


import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.himanshuph.roadzentask.Injection

import com.himanshuph.roadzentask.R
import com.himanshuph.roadzentask.data.model.RequestDetails
import com.himanshuph.roadzentask.utils.gone
import com.himanshuph.roadzentask.utils.inflate
import com.himanshuph.roadzentask.utils.rx.AppSchedulerProvider
import com.himanshuph.roadzentask.utils.visible
import kotlinx.android.synthetic.main.fragment_company_detail_request.*
import com.himanshuph.roadzentask.data.model.Question


class CompanyDetailRequestFragment : Fragment(), CompanyContract.View {

    var mPresenter: CompanyContract.Presenter? = null
    lateinit var button1: Button
    lateinit var button2: Button
    var mCompanyTILInfoList : ArrayList<TextInputInfo> = ArrayList()
    var mRequesterTILInfoList : ArrayList<TextInputInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = CompanyRequestPresenter(Injection.provideAppDataManager(), AppSchedulerProvider())
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

    override fun showLoading() {
        progressBar.visible()
        errorTv.gone()
    }

    override fun showCompanyFormView(requestDetails: RequestDetails) {
        val textView = getHeaderTextView(requestDetails)
        ll.addView(textView)
        val questionList = requestDetails.questions
        questionList.forEach { question ->
            addEditText(question)
        }
        progressBar.gone()
        errorTv.gone()
        button1 = Button(context)
        button2 = Button(context)
        button1.setOnClickListener {

        }
    }


    private fun addEditText(question: Question) {
        val editTextlp = getLayoutParams().apply { setMargins(50, 50, 50, 50) }
        val tILP = getLayoutParams()
        val textInputLayout = TextInputLayout(context)
        textInputLayout.apply {
            id = View.generateViewId()
            mCompanyTILInfoList.add(TextInputInfo(id,question.validation))
            hint = question.hint
            layoutParams = tILP
        }

        val editText = EditText(context)
        editText.apply {
            id = View.generateViewId()
            when (question.type) {
                "textNumeric" -> inputType = InputType.TYPE_CLASS_NUMBER
                else -> inputType = InputType.TYPE_CLASS_TEXT
            }
            layoutParams = editTextlp
        }

        textInputLayout.addView(editText, editTextlp)
        ll.addView(textInputLayout, tILP)
    }

    private fun getHeaderTextView(requestDetails: RequestDetails): TextView {
        val textView = TextView(context);
        val lp = getLayoutParams()
        lp.setMargins(25, 25, 25, 25);
        textView.apply {
            text = requestDetails.header
            id = View.generateViewId()
            layoutParams = lp
            gravity = Gravity.CENTER;
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f);
        }
        return textView
    }

    private fun getLayoutParams() = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

    override fun showError() {
        progressBar.gone()
        errorTv.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
    }


    companion object {
        @JvmField
        val TAG = "CompanyFragment"

        @JvmStatic
        fun newInstance(): CompanyDetailRequestFragment {
            val fragment = CompanyDetailRequestFragment()
            return fragment
        }
    }
}
