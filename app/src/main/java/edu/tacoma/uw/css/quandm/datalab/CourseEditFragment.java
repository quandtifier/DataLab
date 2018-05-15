package edu.tacoma.uw.css.quandm.datalab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseEditFragment extends Fragment {

    private final static String COURSE_EDIT_URL
            = "http://cssgate.insttech.washington.edu/~quandm/Android/editCourse.php?";
    //= "http://quandt.000webhostapp.com/Android/editCourse.php?";

    private TextView mCourseIdTextView;
    private EditText mCourseShortDescEditText;
    private EditText mCourseLongDescEditText;
    private EditText mCoursePrereqsEditText;

    private CourseEditListener mListener;


    public CourseEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseEditFragment newInstance(String param1, String param2) {
        CourseEditFragment fragment = new CourseEditFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_course_edit, container, false);

        mCourseIdTextView = (TextView) v.findViewById(R.id.course_to_edit);
        mCourseIdTextView.setText(getArguments().getString("courseToEdit"));
        mCourseShortDescEditText = (EditText) v.findViewById(R.id.change_short_desc);
        mCourseLongDescEditText = (EditText) v.findViewById(R.id.change_long_desc);
        mCoursePrereqsEditText = (EditText) v.findViewById(R.id.change_prereqs);
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        Button editCourseButton = (Button) v.findViewById(R.id.btn_submit);
        editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.editCourse(url);
            }
        });


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CourseEditListener) {
            mListener = (CourseEditListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CourseEditListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(COURSE_EDIT_URL);

        try {

            String courseId = mCourseIdTextView.getText().toString();
            sb.append("id=");
            sb.append(URLEncoder.encode(courseId, "UTF-8"));


            String courseShortDesc = mCourseShortDescEditText.getText().toString();
            sb.append("&shortDesc=");
            sb.append(URLEncoder.encode(courseShortDesc, "UTF-8"));


            String courseLongDesc = mCourseLongDescEditText.getText().toString();
            sb.append("&longDesc=");
            sb.append(URLEncoder.encode(courseLongDesc, "UTF-8"));

            String coursePrereqs = mCoursePrereqsEditText.getText().toString();
            sb.append("&prereqs=");
            sb.append(URLEncoder.encode(coursePrereqs, "UTF-8"));

            Log.i(TAG, sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
    public interface CourseEditListener {
        public void editCourse(String url);
    }
}