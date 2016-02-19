package com.jeson.testpb;

import com.jeson.roster.proto.Roster.Student;
import com.jeson.roster.proto.Roster.Student.Sex;
import com.jeson.roster.proto.Roster.StudentRoster;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InfoHelper {


    public static final String FILE_NAME = "/mnt/sdcard/roster.dat";


    /**
     * 反序列化
     */

    public static List<StudentInfo> getStudentsFromFile() {

        ArrayList<StudentInfo> list = new ArrayList<StudentInfo>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(FILE_NAME);
            StudentRoster roster = StudentRoster.parseFrom(fis);
            int student_count = roster.getStudentCount();

            for (int i = 0; i < student_count; i++) {
                Student student = roster.getStudent(i);
                StudentInfo info = new StudentInfo();
                info.setId(student.getId());
                info.setName(student.getName());
                info.setSex(student.getSex().toString());
                list.add(info);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return list;
    }


    /**
     * 序列化
     */

    public static void saveStudentIntoFile(int id, String name, String sex) {
        FileOutputStream fos = null;

        StudentRoster.Builder rosterBuid = StudentRoster.newBuilder();


        Student.Builder student = Student.newBuilder();
        student.setId(id);
        student.setName(name);
        if ("MALE".equalsIgnoreCase(sex)) {
            student.setSex(Sex.MALE);
        } else {
            student.setSex(Sex.FEMALE);
        }

        rosterBuid.addStudent(student.build());

        try {
            fos = new FileOutputStream(FILE_NAME, true);
            rosterBuid.build().writeTo(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
