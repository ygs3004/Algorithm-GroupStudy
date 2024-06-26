package com;

import com.Week00.S3_14501.Q14501;
import com.Week00.S4_1158.Q1158;
import com.Week00.S4_2776.Q2776;
import com.Week00.S4_3986.Q3986;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class RunProblem {

    public static final String INPUT_DIR = "in";
    public static final String OUTPUT_DIR = "out";
    public static final String RESULT_FILE_NAME = "result.txt";
    public static int passCnt = 0;

    public static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws Exception {
        Solution sol = new Q3986();

        List<File> inputFiles = Arrays.stream(new File(sol.getClass().getResource(INPUT_DIR).toURI()).listFiles())
                                      .sorted(Comparator.comparing(File::getName))
                                      .collect(Collectors.toList());
        List<File> outputFiles = Arrays.stream(new File(sol.getClass().getResource(OUTPUT_DIR).toURI()).listFiles())
                                       .sorted(Comparator.comparing(File::getName))
                                       .collect(Collectors.toList());

        for(int i = 0; i < inputFiles.size(); i++) {
            sb.append("========== [Test Case(" + (i + 1) + ") Result] ==========\n");
            run(sol, inputFiles.get(i).getPath(), outputFiles.get(i).getPath());
        }
        sb.append("===========================================\n");
        sb.append("Total: " + inputFiles.size() + "개  Pass: " + passCnt + "개");

        System.out.println(sb);
    }

    public static void run(Solution sol, String inFileName, String outFileName) throws Exception {
        InputStream originalInputStream = System.in;  // 원본 System.in 백업
        PrintStream originalOut = System.out;  // 원본 System.out 백업

        try(FileInputStream inputFileStream = new FileInputStream(inFileName);
             PrintStream resultOutStream = new PrintStream(new FileOutputStream(RESULT_FILE_NAME))) {

            System.setIn(inputFileStream);  // 파일 입력을 System.in 으로 리디렉션
            System.setOut(resultOutStream);  // 임시 파일에 출력

            Long srtTime = System.currentTimeMillis();
            sol.solution();

            sb.append("Execution Time: " + (System.currentTimeMillis() - srtTime) + " ms")
              .append(System.lineSeparator());

            resultOutStream.flush();  // 출력 스트림 강제 비우기
        } finally {
            System.setIn(originalInputStream);  // 원래의 System.in 복원
            System.setOut(originalOut);  // 원래의 System.out 복원
        }

        try(BufferedReader expectedReader = new BufferedReader(new FileReader(outFileName));
             BufferedReader actualReader = new BufferedReader(new FileReader(RESULT_FILE_NAME))) {

            boolean isOk = true;
            String expected;
            String actual;

            while((expected = expectedReader.readLine()) != null) {
                actual = actualReader.readLine();

                if(actual == null || !expected.equals(actual)) {
                    isOk = false;
                }
            }

            if(actualReader.readLine() != null) {
                isOk = false;
            }

            sb.append("Result: ")
              .append(isOk ? "Pass" : "Fail")
              .append(System.lineSeparator());
            passCnt += isOk ? 1 : 0;
        } finally {
            Files.delete(Paths.get(RESULT_FILE_NAME));
        }
    }
}