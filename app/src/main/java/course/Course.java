package edu.tacoma.uw.css.quandm.datalab.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    public static final String ID = "id";
    public static final String SHORT_DESC = "shortDesc";
    public static final String LONG_DESC = "longDesc";
    public static final String PRE_REQS = "prereqs";


    private String mCourseId;
    private String mShortDescription;
    private String mLongDescription;
    private String mPrereqs;

    public Course (String theCourseId, String theShortDescription, String theLongDescription, String thePrereqs) {
        mCourseId = theCourseId;
        mShortDescription = theShortDescription;
        mLongDescription = theLongDescription;
        mPrereqs = thePrereqs;
    }

    public static List<Course> parseCourseJSON(String courseJSON) throws JSONException {
        List<Course> courseList = new ArrayList<Course>();
        if (courseJSON != null) {

            JSONArray arr = new JSONArray(courseJSON);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Course course = new Course(obj.getString(Course.ID), obj.getString(Course.SHORT_DESC)
                        , obj.getString(Course.LONG_DESC), obj.getString(Course.PRE_REQS));
                courseList.add(course);
            }

        }

        return courseList;
    }
    public String getCourseId() {
        return mCourseId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getLongDescription() {
        return mLongDescription;
    }

    public String getPrereqs() {
        return mPrereqs;
    }

    public void setCourseID(String mCourseID) {
        this.mCourseId = mCourseID;
    }

    public void setShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public void setLongDescription(String mLongDescription) {
        this.mLongDescription = mLongDescription;
    }

    public void setPrereqs(String mPrereqs) {
        this.mPrereqs = mPrereqs;
    }
}