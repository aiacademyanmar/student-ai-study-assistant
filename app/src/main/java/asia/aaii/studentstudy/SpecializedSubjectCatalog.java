package asia.aaii.studentstudy;
/** V2.42: app-created specialization catalog; not an official university syllabus. */
public final class SpecializedSubjectCatalog {
 private SpecializedSubjectCatalog(){}
 public static final String[] GROUPS={"Myanmar University","Engineering","Medicine","Management / Business"};
 public static final String[][] SUBJECTS={
  {"Physics","Chemistry","Mathematics","English","Myanmar","Zoology","Botany","Geology","Geography","History","Economics","Philosophy","Psychology","Law","Computer Science","Statistics","Environmental Science","Microbiology","Biochemistry","Biotechnology","Political Science","International Relations","Sociology","Anthropology","Education","Library and Information Science"},
  {"Civil Engineering","Mechanical Engineering","Electrical Power Engineering","Electronic Engineering","Computer Engineering","Mechatronics Engineering","Chemical Engineering","Architecture","Industrial Engineering","Petroleum Engineering","Mining Engineering","Marine Engineering","Telecommunications Engineering","Renewable Energy Engineering"},
  {"Human Anatomy","Human Physiology","Biochemistry for Medicine","Pathology","Pharmacology","Microbiology for Medicine","Immunology","Community Medicine","Internal Medicine","General Surgery","Pediatrics","Obstetrics and Gynecology","Psychiatry","Medical Ethics","Clinical Research Methods"},
  {"Business Management","Accounting","Finance","Marketing","Human Resource Management","Operations Management","Entrepreneurship","Project Management","Organizational Behavior","Strategic Management","Supply Chain Management","Business Analytics"}
 };
 public static final String[] MODES={
 "AI Teaching","Concept Summary","Worked Examples","Interactive Practice","Original MCQ Quiz",
 "Written Practice","Case Study","Critical Thinking","Exam Revision","7-Day Study Plan"
 };
 public static String build(int group,int subject,int mode,String topic,String language){
  if(group<0||group>=SUBJECTS.length||subject<0||subject>=SUBJECTS[group].length)throw new IllegalArgumentException("subject");
  if(mode<0||mode>=MODES.length)throw new IllegalArgumentException("mode");
  String name=SUBJECTS[group][subject];
  String focus=topic==null||topic.trim().isEmpty()?"Ask the learner to select one topic":topic.trim();
  if(focus.length()>240)focus=focus.substring(0,240);
  String lang="English".equals(language)?"English":"Myanmar Unicode with accurate English technical terms";
  return "ROLE: Patient, rigorous higher-education learning assistant.\n"+
   "STUDENT SPECIALIZATION (app-authored, not an official institutional syllabus): "+GROUPS[group]+" → "+name+"\n"+
   "TASK: "+MODES[mode]+"\n"+
   "LEARNER TOPIC (unverified): "+focus+"\n"+
   "LANGUAGE: "+lang+"\n"+
   "RULES: Ask the student about prior knowledge and university/year before detailed teaching. Explain with original examples, define technical terms, work step by step and ask ONE question at a time. "+
   "Use formative feedback and a short recap. Do not fabricate university accreditation, university syllabus outcomes, exams, official marks, research citations, or textbook contents. "+
   "Clearly disclose unknown facts and distinguish examples from verified facts. "+
   "For medicine, use educational non-patient scenarios and emphasize that clinical decisions require qualified supervision. "+
   "Avoid personalized diagnosis, drug dosing or treatment instructions. "+
   "For engineering, disclose assumptions and require professional verification before safety-critical real-world use.";
 }
 public static int count(){int n=0;for(String[] a:SUBJECTS)n+=a.length;return n;}
}
