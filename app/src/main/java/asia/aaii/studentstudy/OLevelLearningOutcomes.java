package asia.aaii.studentstudy;

/** Plain-language ORIGINAL outcome interpretations, not Cambridge verbatim objectives. */
public final class OLevelLearningOutcomes {
  private OLevelLearningOutcomes(){}
  public static String status(OLevel2026.Subject s) {
    return "Topic grouping cross-checked against Cambridge syllabus overview for exam year 2026. "+
           "Below: ORIGINAL APP learning objectives, NOT official outcome statements "+
           "or a guarantee of full clause-by-clause syllabus coverage.";
  }
  public static String contentOutcome(OLevel2026.Subject s,OLevel2026.Topic t,int sub) {
    String focus=t.subtopics[sub];
    String goal;
    switch(s.code){
      case "1123":
        goal="Recognise purpose, audience and language choices; develop an accurate, organised response in an appropriate style.";
        break;
      case "4024":
        goal="Choose a suitable mathematical method; communicate clear steps, units and reasoning; check answers for plausibility. In Paper 1 work without a calculator.";
        break;
      case "5070":
        goal="Use correct chemical terminology, particle explanations and balanced equations; where needed interpret results and apply safe experimental reasoning.";
        break;
      case "5054":
        goal="Explain the physical principle with correct quantities and units; use diagrams/equations to solve unfamiliar problems and evaluate evidence.";
        break;
      case "2281":
        goal="Define the economic concept, illustrate it using a market or policy example, apply diagrams and data, then evaluate the consequences.";
        break;
      case "2147":
        goal="Explain causes and consequences using historical evidence; compare interpretations and evaluate the usefulness of sources in their context.";
        break;
      case "2217":
        goal="Describe geographical patterns and processes, interpret maps or data, and use an appropriate case study to support an evaluated conclusion.";
        break;
      default:
        goal="Explain, apply and evaluate the topic using relevant examples and evidence.";
    }
    return "LEARNING OUTCOME · APP STUDY TARGET\n"+
        "Subtopic: "+focus+"\n"+
        "1. Explain or define '"+focus+"' accurately in Burmese with English keywords.\n"+
        "2. "+goal+"\n"+
        "3. Answer at least one original context-based question and justify the response.\n"+
        "These are app-authored teaching targets. Check the official PDF for exact learning-outcome scope.";
  }
  public static String examConstraints(String code){
    switch(code){
      case "1123":return "Paper 1 Reading and Paper 2 Writing (2 hours each). Build strong inference/summary and written register; do not confuse 2024–2026 with 2027 syllabus.";
      case "4024":return "4024 has 9 official content groups; Paper 1: non-calculator, Paper 2: calculator. 2026 availability: zones 3,4,5; confirm your exam centre.";
      case "5070":return "Chemistry Paper 1 MCQ, Paper 2 Theory, plus Paper 3 Practical OR Paper 4 Alternative to Practical; teach safe experimental skills.";
      case "5054":return "Physics Paper 1 MCQ, Paper 2 Theory, plus Practical or Alternative to Practical; SI units, calculations, graphs and experimental uncertainties.";
      case "2281":return "Economics Paper 1 MCQ (30%), Paper 2 structured responses (70%); practise analysis and supported evaluation.";
      case "2147":return "History OPTION RULE: study EITHER Core Option A OR Core Option B, AND at least ONE Depth Study A–E; all seven study groups are NOT compulsory. "+
        "2026 Paper 2 prescribed topics: June Option A US Civil War / Option B Treaty of Versailles; "+
        "November Option A Italian unification / Option B US containment of communism. "+
        "Match your exam series and chosen Core option before studying.";
      case "2217":return "Geography: 3 official THEMES and Paper 1 themes, Paper 2 skills, Paper 3 investigations. "+
        "Case studies, maps, fieldwork-style data interpretation and evaluative judgement are required. "+
        "2026 availability varies by administrative zone; confirm with centre.";
      default:return "Always check component options, syllabus year and administrative zone.";
    }
  }
  public static String lessonPrompt(OLevel2026.Subject s,OLevel2026.Topic t,int sub,int stage,String mode){
    String focus=t.subtopics[sub];
    String common="You are a Cambridge O Level 2026-orientated tutor. "+
      "Subject: "+s.name+" ("+s.code+"). Official heading: "+t.name+
      ". App learning subtopic: "+focus+". Lesson stage: "+OLevel2026.LESSON_STAGES[stage]+". "+
      "\nAssessment constraints: "+examConstraints(s.code)+
      "\nOriginal app learning targets:\n"+contentOutcome(s,t,sub)+
      "\nTeach in clear Myanmar Unicode with English technical terms. "+
      "Do not claim these learning targets are verbatim Cambridge requirements. "+
      "Do not invent official past papers, examiner marks or precise syllabus clauses. "+
      "If the topic depends on choice/zone/series, say which choice applies. ";
    String action;
    switch(mode){
      case "practice":
        action="Interactive practice: ask 6 ORIGINAL exam-relevant questions ONE AT A TIME, "+
          "including 2 concept, 2 structured and 2 application/source/data questions if appropriate. "+
          "Wait for each student answer, give hints and corrections, score 0–2 per question (12 total) "+
          "and provide model reasoning AFTER the answer; conclude with targets for improvement.";
        break;
      case "exam":
        action="Exam practice: explain the relevant paper component and create 5 ORIGINAL "+
          "questions with marks and time guidance, aligned to this selected level, paper type "+
          "and learning outcome. Ask only Q1 initially, wait for each answer, mark with a "+
          "clear self-generated rubric and finish with worked solutions.";
        break;
      case "practical":
        action="Practical and skills: create 4 subject-appropriate ORIGINAL experiments, "+
          "data, map, source interpretation or writing tasks as relevant, without fabricating "+
          "unsafe physical procedures. Explain variables, ethical/safety considerations and "+
          "how to evaluate accuracy, plus practice marking.";
        break;
      case "revision":
        action="First provide a Burmese summary with 5 central takeaways, glossary, "+
          "5 specific exam tips, a common-errors checklist, then ask 4 active-recall "+
          "questions ONE AT A TIME, with hints, scoring and a short revision plan.";
        break;
      default:
        action="Start teaching NOW. Explain learning objectives, key theory, definitions, "+
          "English keywords, 2 fully-worked original examples, 3 common mistakes, "+
          "3 exercises with model answers and a short summary. "+
          "Do not make the student wait to get this complete initial explanation.";
    }
    return common+"\nTask:\n"+action;
  }
}
