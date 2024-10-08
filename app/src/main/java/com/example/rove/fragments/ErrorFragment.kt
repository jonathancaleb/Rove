package com.example.rove.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.rove.RoveConstants
import com.example.rove.R
import com.example.rove.databinding.FragmentErrorBinding
import com.example.rove.ui.UIControlInterface

/**
 * A simple [Fragment] subclass.
 * Use the [ErrorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ErrorFragment : Fragment() {

    private var _errorFragmentBinding: FragmentErrorBinding? = null

    private lateinit var mUIControlInterface: UIControlInterface

    private var mErrorString = R.string.perm_rationale
    private var mErrorIcon = R.drawable.ic_folder_music

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getString(TAG_ERROR)?.let { errorType ->

             when (errorType) {
                RoveConstants.TAG_NO_MUSIC -> {
                    mErrorIcon = R.drawable.ic_music_off
                    mErrorString = R.string.error_no_music
                }
                RoveConstants.TAG_NO_MUSIC_INTENT -> {
                    mErrorIcon = R.drawable.ic_mood_bad
                    mErrorString = R.string.error_unknown_unsupported
                }
                RoveConstants.TAG_SD_NOT_READY -> {
                    mErrorIcon = R.drawable.ic_mood_bad
                    mErrorString = R.string.error_not_ready
                }
            }
        }

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mUIControlInterface = activity as UIControlInterface
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _errorFragmentBinding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _errorFragmentBinding = FragmentErrorBinding.inflate(inflater, container, false)
        return _errorFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _errorFragmentBinding?.run {
            errorMessage.text = getString(mErrorString)
            errorIcon.setImageResource(mErrorIcon)
            root.setOnClickListener { mUIControlInterface.onCloseActivity() }

            errorToolbar.setNavigationOnClickListener {
                mUIControlInterface.onCloseActivity()
            }
        }
    }

    companion object {

        private const val TAG_ERROR = "WE_HAVE_A_PROBLEM_HOUSTON"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ErrorFragment.
         */
        @JvmStatic
        fun newInstance(errorType: String) = ErrorFragment().apply {
            arguments = bundleOf(TAG_ERROR to errorType)
        }
    }
}
