package com.aimanager.agent.utils;

import com.aimanager.agent.graph.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import static com.aimanager.agent.utils.YesNoIDK.YES;
import static com.aimanager.agent.utils.YesNoIDK.NO;
import static com.aimanager.agent.utils.YesNoIDK.I_DONT_KNOW;
import static com.aimanager.agent.utils.YesNoIDK.Y;
import static com.aimanager.agent.utils.YesNoIDK.N;
import static com.aimanager.agent.utils.YesNoIDK.I;


public class Utils {

  public static  List<Pair<Integer, String>> zipWithIndex(Set<String> set) {
    List<String> list = new ArrayList<>(set);
    return IntStream.range(0, list.size())
        .mapToObj(i -> new Pair<>(i, list.get(i)))
        .collect(Collectors.toList());
  }

  public static void yesNoPrompt(String s) {
    System.out.println(s);
    System.out.println("Please answer with 'yes/y' or 'no/n' or 'I don't know/idk'");
  }

  public static YesNoIDK askForYesNoAnswer(String s) {
    while (true) {
      yesNoPrompt(s);
      String answer = System.console().readLine();
      if (answer.equalsIgnoreCase(YES.getName()) || answer.equalsIgnoreCase(Y.getName())) {
        return YES;
      } else if (answer.equalsIgnoreCase(NO.getName()) || answer.equalsIgnoreCase(N.getName())) {
        return NO;
      } else if (answer.equalsIgnoreCase(I_DONT_KNOW.getName()) || answer.equalsIgnoreCase(I.getName())) {
        return I;
      }
    }
  }

  public static String cleanString(String question) {
   if(StringUtils.isNotEmpty(question)) {
    // Remove question marks and punctuation from the string and do not remove whitespace
    String q = question.replaceAll("[^a-zA-Z0-9\\s]", "");

      return q;
    }
    return question;
  }

  public static boolean containsOnlyDigits(String s) {
    return s.matches("[0-9]+");
  }

}
