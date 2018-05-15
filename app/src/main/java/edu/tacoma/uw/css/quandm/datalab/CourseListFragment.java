package edu.tacoma.uw.css.quandm.datalab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import data.CourseDB;
import edu.tacoma.uw.css.quandm.datalab.course.Course;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CourseListFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String COURSE_URL
            ="http://cssgate.insttech.washington.edu/~quandm/Android/list.php?cmd=courses";
    //="http://quandt.000webhostapp.com/Android/list.php?cmd=courses";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Course> mCourseList;
    private RecyclerView mRecyclerView;
    private CourseDB mCourseDB;

    //private View mLoadingView;
    //private int mLongAnimationDuration;




    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseListFragment() {
    }


    public static CourseListFragment newInstance(int columnCount) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        //mLoadingView = getActivity().findViewById(R.id.loading_spinner);

        // Retrieve and cache the system's default "short" animation time.
        //mLongAnimationDuration = getResources().getInteger(
        //        android.R.integer.config_longAnimTime);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            FloatingActionButton floatingActionButton = (FloatingActionButton)
                    getActivity().findViewById(R.id.fab);
            floatingActionButton.show();

            ConnectivityManager connMgr = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                CourseAsyncTask courseAsyncTask = new CourseAsyncTask();
                courseAsyncTask.execute(new String[]{COURSE_URL});
            }
            else {
                Toast.makeText(view.getContext(),
                        "No network connection available. Displaying locally stored data",
                        Toast.LENGTH_SHORT).show();

                if (mCourseDB == null) {
                    mCourseDB = new CourseDB(getActivity());
                }
                if (mCourseList == null) {
                    mCourseList = mCourseDB.getCourses();
                }

                mRecyclerView.setAdapter(new MyCourseRecyclerViewAdapter(mCourseList, mListener));
            }

        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    private void crossfade() {
//
//
//
//        // Animate the loading view to 0% opacity. After the animation ends,
//        // set its visibility to GONE as an optimization step (it won't
//        // participate in layout passes, etc.)
//        mLoadingView.animate()
//                .alpha(0f)
//                .setDuration(mLongAnimationDuration)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mLoadingView.setVisibility(View.GONE);
//                    }
//                });
//
//        // Set the content view to 0% opacity but visible, so that it is visible
//        // (but fully transparent) during the animation.
//        mRecyclerView.setAlpha(0f);
//        mRecyclerView.setVisibility(View.VISIBLE);
//
//        // Animate the content view to 100% opacity, and clear any animation
//        // listener set on the view.
//        mRecyclerView.animate()
//                .alpha(1f)
//                .setDuration(mLongAnimationDuration)
//                .setListener(null);
//    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Course item);
    }

    private class CourseAsyncTask extends AsyncTask<String, Void, String> {




        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the list of courses, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                mCourseList = Course.parseCourseJSON(result);
            }
            catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!mCourseList.isEmpty()) {

                if (mCourseDB == null) {
                    mCourseDB = new CourseDB(getActivity());
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                mCourseDB.deleteCourses();

                // Also, add to the local database
                for (int i=0; i<mCourseList.size(); i++) {
                    Course course = mCourseList.get(i);
                    mCourseDB.insertCourse(course.getCourseId(),
                            course.getShortDescription(),
                            course.getLongDescription(),
                            course.getPrereqs());
                }
                mRecyclerView.setAdapter(new MyCourseRecyclerViewAdapter(mCourseList, mListener));
            }
        }


    }
}