package asia.aaii.studentstudy;

/**
 * Each prompt uses the 2026 paper and the selected module only.
 * AI answers are provided by the user's chosen external or configured provider;
 * they are not bundled in the APK and may be inaccurate.
 */
public final class ALevelPromptFactory{
 private ALevelPromptFactory(){}
 public static String create(ALevel2026.Subject s,ALevel2026.Module m,String topic,int stage,String mode){
  String intro="You are a highly capable Cambridge International AS & A Level 2026 "
   +"tutor. Teach in MYANMAR UNICODE, retaining English subject-specific technical terms. "
   +"Do not claim affiliation, official endorsement or verification by Cambridge International.\n"
   +"2026-exam SUBJECT: "+s.name+" ("+s.code+").\n"
   +"Relevant 2026 syllabus period: "+s.years+".\n"
   +"Official subject page: "+s.pageUrl+"\n"
   +"Official syllabus PDF/reference: "+s.pdfUrl+"\n"
   +"Course module: "+m.name+"\n"
   +"Level/choice: "+m.level+"; component: "+m.paper+"\n"
   +"App-defined study focus (NOT an official verbatim syllabus outcome): "+topic+"\n"
   +"Lesson stage: "+ALevel2026.LESSON_STAGES[Math.max(0,Math.min(2,stage))]+"\n"
   +"2026 exam notes: "+s.examNotes+"\n"
   +"IMPORTANT: A full A Level usually requires AS plus A-level content, as specified in the syllabus; "
   +"ROUTE and OPTION modules may be mutually exclusive. Confirm selected exam entry route and series. "
   +"Never teach every History/Psychology/Maths alternative as compulsory. "
   +"Do not treat published 2027 or 2029 syllabus changes as examinable in 2026. "
   +"If this topic's precise exam scope is uncertain, explicitly say so and direct the learner "
   +"to the official 2026 PDF. Do not invent page numbers, named core studies or syllabus learning outcome IDs. "
   +"Use new ORIGINAL questions; do not reproduce or label them as official past-paper questions.\n";
  String task;
  switch(mode){
   case "practice":
    task="Interactive Exercises: teach a 3-line concept review then prepare 8 original questions with "
       +"ascending difficulty, a mix of concept, numerical/data, source analysis, essay or coding "
       +"AS APPROPRIATE TO THIS SUBJECT. Ask Q1 only, wait for student reply, mark using an explicitly "
       +"teacher-created points rubric, show hints and feedback and then continue one-by-one. "
       +"At the end give a weak-skills list and actionable revision targets.";break;
   case "exam":
    task="2026 AS/A Level exam practice aligned with THIS chosen module and PAPER, "
       +"produce a compact mock set of 5 ORIGINAL questions (not real past papers), "
       +"show estimated timing and self-created mark breakdown. Ask only Q1 now, "
       +"evaluate answer with justified feedback; proceed one question at a time, "
       +"finish with full solutions and a learning plan. "
       +"Respect optional component selections and practical requirements.";break;
   case "practical":
    task="Practical/data/source/research mode: create 4 safe, original, subject-appropriate "
       +"tasks (science planning+data analysis, computer programming/pseudocode, financial data, "
       +"historical source judgement, psychological study design, English analysis as appropriate). "
       +"Explain variables, assumptions, accuracy, reliability and ethics if relevant. "
       +"A paper exercise does not substitute for supervised practical assessment.";break;
   case "revision":
    task="Revision: provide a concise Burmese concept map, 10 English exam keywords with "
       +"Burmese definitions, 5 common mistakes and 5 exam tips, and start a 5-question "
       +"active recall session by asking the first question, awaiting each answer before scoring.";break;
   default:
    task="Start the lesson immediately and fully: 1) original syllabus-oriented learning "
       +"objectives (NOT verbatim official clauses), 2) plain-language Burmese explanation with "
       +"English keywords, 3) 2 fully worked realistic examples with all reasoning, 4) "
       +"important calculations, formulas or source evidence AS APPLICABLE, 5) "
       +"3 misconceptions with corrections, 6) three original quick-check exercises and "
       +"model answers, 7) concise revision notes and the relevant paper-specific exam tips. "
       +"Be clear about AS versus A-level depth.";
  }
  return intro+"\nINSTRUCTIONS:\n"+task;
 }
}
