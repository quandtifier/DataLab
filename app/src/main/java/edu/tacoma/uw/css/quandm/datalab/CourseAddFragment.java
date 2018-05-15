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
import android.widget.Toast;

import java.net.URLEncoder;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseAddFragment extends Fragment {
    private final static String COURSE_ADD_DEBUG = "CourseAddFragment: ";
    private final static String COURSE_ADD_URL
            = "http://cssgate.insttech.washington.edu/~quandm/Android/addCourse.php?";
    //= "http://quandt.000webhostapp.com/Android/addCourse.php?";

    private EditText mCourseIdEditText;
    private EditText mCourseShortDescEditText;
    private EditText mCourseLongDescEditText;
    private EditText mCoursePrereqsEditText;

    private CourseAddListener mListener;

    public CourseAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseAddFragment.
     */

    public static CourseAddFragment newInstance(String courseID) {
        CourseAddFragment fragment = new CourseAddFragment();
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
        View v = inflater.inflate(R.layout.fragment_course_add, container, false);

        mCourseIdEditText = (EditText) v.findViewById(R.id.edit_courseid);
        mCourseShortDescEditText = (EditText) v.findViewById(R.id.edit_short_desc);
        mCourseLongDescEditText = (EditText) v.findViewById(R.id.edit_long_desc);
        mCoursePrereqsEditText = (EditText) v.findViewById(R.id.edit_prereqs);
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        Button addCourseButton = (Button) v.findViewById(R.id.btn_course);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.addCourse(url);
            }
        });


        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
        if (context instanceof CourseAddListener) {
            mListener = (CourseAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CourseAddListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(COURSE_ADD_URL);

        try {

            String courseId = mCourseIdEditText.getText().toString();
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
        Log.v(COURSE_ADD_DEBUG, sb.toString());
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

    public interface CourseAddListener {
        public void addCourse(String url);
    }
}