package asia.aaii.studentstudy;
import java.util.HashMap;
import java.util.Map;
/**
 * Paper names and broad format cues for Cambridge AS/A 2026 learning.
 * Not a substitute for exact 2026 syllabus assessment or selected route rules.
 * Practice duration/marks are self-generated; never represented as official paper data.
 */
public final class ALevelPaperCatalog {
  private ALevelPaperCatalog(){}
  public static final class Paper{
    public final String number,stage,title,format;
    public Paper(String n,String s,String t,String f){number=n;stage=s;title=t;format=f;}
    public boolean isOption(){return stage.contains("OPTION")||stage.contains("ROUTE");}
    public String label(){return "Paper "+number+" · "+title;}
  }
  private static final Map<String,Paper[]> ALL=new HashMap<>();
  static {
      put("9093",new Paper[]{
        new Paper("1","AS","Reading","textual evidence, inference, comparison and analytical commentary"),
        new Paper("2","AS","Writing","imaginative, descriptive, discursive and directed writing"),
        new Paper("3","A","Language Analysis","language variation, text/discourse analysis"),
        new Paper("4","A","Language Topics","argument and evaluation of topics in language study")
      });
      put("9231",new Paper[]{
        new Paper("1","AS","Further Pure Mathematics 1","advanced proofs and mathematical reasoning"),
        new Paper("2","A","Further Pure Mathematics 2","further pure mathematics"),
        new Paper("3","ROUTE","Further Mechanics","advanced mechanics modelling"),
        new Paper("4","ROUTE","Further Probability & Statistics","statistical inference and probability")
      });
      put("9489",new Paper[]{
        new Paper("1","AS_OPTION","Document Question","sources and contextual interpretation"),
        new Paper("2","AS_OPTION","Outline Study","causation and contextual essay"),
        new Paper("3","A_OPTION","Interpretations Question","historian's interpretation and approach"),
        new Paper("4","A_OPTION","Depth Study","analytical essays for selected historical route")
      });
      put("9609",new Paper[]{
        new Paper("1","AS","Business Concepts 1","business concepts and short answers"),
        new Paper("2","AS","Business Concepts 2","case application and extended responses"),
        new Paper("3","A","Business Decision-Making","business data and decision evaluation"),
        new Paper("4","A","Business Strategy","longer integrated strategic analysis")
      });
      put("9618",new Paper[]{
        new Paper("1","AS","Theory Fundamentals","computer science systems and data"),
        new Paper("2","AS","Fundamental Problem-solving and Programming Skills","algorithms, pseudocode and trace tables"),
        new Paper("3","A","Advanced Theory","advanced computer science systems"),
        new Paper("4","A","Practical","original programming and testing tasks")
      });
      put("9700",new Paper[]{
        new Paper("1","AS","Multiple Choice","subject content multiple choice"),
        new Paper("2","AS","AS Level Structured Questions","short structured theory and calculations"),
        new Paper("3","AS","Advanced Practical Skills","practical planning, observation and data tasks"),
        new Paper("4","A","A Level Structured Questions","advanced extended structured response"),
        new Paper("5","A","Planning, Analysis and Evaluation","experiment design and evaluation")
      });
      put("9701",new Paper[]{
        new Paper("1","AS","Multiple Choice","chemical principles multiple choice"),
        new Paper("2","AS","AS Level Structured Questions","balanced equations, calculations, analysis"),
        new Paper("3","AS","Advanced Practical Skills","qualitative tests, titration and lab skills"),
        new Paper("4","A","A Level Structured Questions","advanced inorganic and organic chemistry"),
        new Paper("5","A","Planning, Analysis and Evaluation","experimental design, errors, treatment of data")
      });
      put("9702",new Paper[]{
        new Paper("1","AS","Multiple Choice","theory, calculations and application"),
        new Paper("2","AS","AS Level Structured Questions","physical principles, graphs and equations"),
        new Paper("3","AS","Advanced Practical Skills","measurement, graphs and uncertainty"),
        new Paper("4","A","A Level Structured Questions","advanced physics and mathematical reasoning"),
        new Paper("5","A","Planning, Analysis and Evaluation","practical design, graphs and evaluation")
      });
      put("9706",new Paper[]{
        new Paper("1","AS","Multiple Choice","accounting principles"),
        new Paper("2","AS","Fundamentals of Accounting","structured bookkeeping and accounts"),
        new Paper("3","A","Financial Accounting","statements, adjustments and interpretation"),
        new Paper("4","A","Cost and Management Accounting","costing, budgeting, investment appraisal")
      });
      put("9708",new Paper[]{
        new Paper("1","AS","AS Level Multiple Choice","economics principles and reasoning"),
        new Paper("2","AS","AS Level Data Response and Essays","data response, micro and macro essays"),
        new Paper("3","A","A Level Multiple Choice","advanced micro and macroeconomics"),
        new Paper("4","A","A Level Data Response and Essays","advanced evidence-based evaluation and essays")
      });
      put("9709",new Paper[]{
        new Paper("1","AS","Pure Mathematics 1","worked pure-mathematics calculations and proofs"),
        new Paper("2","AS_ROUTE","Pure Mathematics 2","pure mathematics route-specific problems"),
        new Paper("3","A","Pure Mathematics 3","advanced pure-mathematics derivations"),
        new Paper("4","ROUTE","Mechanics","mechanics mathematical modelling"),
        new Paper("5","ROUTE","Probability & Statistics 1","statistics and probability problems"),
        new Paper("6","ROUTE","Probability & Statistics 2","statistical inference and applications")
      });
      put("9990",new Paper[]{
        new Paper("1","AS","Approaches, Issues and Debates","core study application and evaluation"),
        new Paper("2","AS","Research Methods","experimental designs, methods and findings"),
        new Paper("3","A_OPTION","Specialist Options: Approaches, Issues and Debates","two selected psychology specialist options"),
        new Paper("4","A_OPTION","Specialist Options: Application and Research Methods","two selected options and research design")
      });
  }
  private static void put(String code,Paper[] papers){ALL.put(code,papers);}
  public static Paper[] forSubject(String code){
    Paper[] p=ALL.get(code);return p==null?new Paper[0]:p;
  }
  public static String examPrompt(ALevel2026.Subject subject,Paper paper,String studyFocus,int practiceMinutes,int questionCount,String specialOption){
    String option=specialOption==null||specialOption.trim().length()==0?
      "OPTION NOT DECLARED. If this paper requires a route/specialist topic, ask student to verify and choose it; do not assume all routes.":specialOption.trim();
    return "You are a GCE Cambridge AS/A Level 2026 exam-preparation tutor. "+
     "Explain instructions and feedback in Myanmar Unicode, with English exam terms.\n"+
     "Subject: "+subject.name+" ("+subject.code+"). 2026 syllabus applicability: "+subject.years+".\n"+
     "Chosen component: Paper "+paper.number+" "+paper.title+" ("+paper.stage+").\n"+
     "Broad assessment focus: "+paper.format+".\n"+
     "Student-selected subtopic: "+(studyFocus==null||studyFocus.isEmpty()?"mixed syllabus-relevant revision":studyFocus)+".\n"+
     "Student-selected route/option: "+option+".\n"+
     "Practice time budget: "+practiceMinutes+" minutes; "+
     "number of original questions: "+questionCount+". This is NOT the official paper duration or marks.\n"+
     "Official reference: "+subject.pageUrl+"\n"+
     "Assessment notes: "+subject.examNotes+"\n"+
     "Create ORIGINAL paper-style practice with clear self-created question marks and an "+
     "appropriate mix of MCQ, calculations, sources, essays, coding, practical planning or "+
     "data interpretation for THIS PARTICULAR PAPER. Do NOT invent exact official mark schemes, "+
     "paper details, date/zone rules or syllabus clause IDs. Do not quote actual past papers. "+
     "If choice/route has not been selected, first request it; do not claim all alternatives are mandatory. "+
     "For practical components, give only safe simulated planning/data-analysis tasks and "+
     "explicitly say that digital work cannot substitute for a supervised laboratory or computing practical. "+
     "Show one question at a time, WAIT for the student's answer, then mark against a "+
     "clearly labelled sample rubric, explain errors, and proceed. At the end report an "+
     "indicative score (not an official grade), worked solutions, topic weaknesses and revision actions.";
  }
}
