package asia.aaii.studentstudy;

/**
 * 2026 exam-year SOURCE INDEX for the 12 O Level tracks in the app.
 * Reference pages/assessment component names checked against Cambridge syllabus PDFs.
 * App-created subtopic/lesson paths are NOT reproduced Cambridge curriculum text.
 */
public final class Cambridge2026Audit {
  private Cambridge2026Audit(){}
  public static final class Source {
    public final String code,validYears,pdfUrl,examPapers,notes;
    public Source(String code,String years,String link,String papers,String guidance){
      this.code=code;this.validYears=years;pdfUrl=link;examPapers=papers;notes=guidance;
    }
  }
  public static final Source[] SOURCES={
    new Source("1123", "2024–2026", "https://www.cambridgeinternational.org/Images/634453-2024-2026-syllabus.pdf", "Paper 1 Reading (2h, 50 marks, 50%). Paper 2 Writing (2h, 50 marks, 50%).", "Both components compulsory; reading texts and directed/composition writing."),
    new Source("4024", "2025–2027", "https://www.cambridgeinternational.org/Images/662480-2025-2027-syllabus.pdf", "Paper 1 Non-calculator. Paper 2 Calculator.", "Calculators are not allowed in Paper 1. Show non-calculator methods where relevant."),
    new Source("4037", "2025–2027", "https://www.cambridgeinternational.org/Images/662720-2025-2027-syllabus.pdf", "Paper 1 Non-calculator (2h, 80 marks, 50%). Paper 2 Calculator (2h, 50%).", "Check formula list and non-calculator reasoning."),
    new Source("5090", "2026–2028", "https://www.cambridgeinternational.org/Images/697330-2026-2028-syllabus.pdf", "Paper 1 MCQ (1h, 40 marks, 30%), Paper 2 Theory (1h45, 80 marks, 50%); Paper 3 Practical OR Paper 4 Alternative to Practical (20%).", "Practical skills, planning, data processing, safety and microscopy are assessed."),
    new Source("5070", "2026–2028", "https://www.cambridgeinternational.org/Images/697326-2026-2028-syllabus.pdf", "Paper 1 MCQ (1h, 40 marks, 30%), Paper 2 Theory (1h45, 80 marks, 50%); Paper 3 Practical OR Paper 4 Alternative to Practical (20%).", "Practical skills, qualitative analysis and safe experiment planning assessed."),
    new Source("5054", "2026–2028", "https://www.cambridgeinternational.org/Images/697324-2026-2028-syllabus.pdf", "Paper 1 MCQ, Paper 2 Theory; Paper 3 Practical OR Paper 4 Alternative to Practical.", "Practical planning, measurements, graphs and uncertainty matter."),
    new Source("2210", "2026–2028", "https://www.cambridgeinternational.org/Images/697287-2026-2028-syllabus.pdf", "Paper 1 Computer Systems (1h45, 75 marks, 50%); Paper 2 Algorithms, Programming and Logic (1h45, 75 marks, 50%).", "No calculators; includes pseudocode or program-code algorithmic solutions; 2029 revisions are NOT applicable."),
    new Source("7115", "2026", "https://www.cambridgeinternational.org/Images/697338-2026-syllabus.pdf", "Paper 1 Short Answer and Data Response; Paper 2 Case Study.", "Case reasoning, application, analysis, evaluation. The Business 7081 code applies starting 2027, not this 2026 track."),
    new Source("2281", "2026", "https://www.cambridgeinternational.org/Images/697295-2026-syllabus.pdf", "Paper 1 MCQ (45 min, 30 marks, 30%); Paper 2 Structured Questions (2h15, 90 marks, 70%).", "Paper 2 includes compulsory and choice-based questions; graphs and evaluation matter."),
    new Source("7707", "2026", "https://www.cambridgeinternational.org/Images/697340-2026-syllabus.pdf", "Paper 1 MCQ (1h15, 35 marks, 30%); Paper 2 Structured Written (1h45, 100 marks, 70%).", "Emphasise double-entry, interpretation, formats and 2026 ratios."),
    new Source("2147", "2024–2026", "https://www.cambridgeinternational.org/Images/649640-2024-2026-syllabus.pdf", "Paper 1 Structured Questions (2h, 60 marks); Paper 2 Document Questions.", "Choose Core OPTION A or Core OPTION B, plus at least ONE depth study. Do not teach all as mandatory."),
    new Source("2217", "2026", "https://www.cambridgeinternational.org/Images/697292-2026-syllabus.pdf", "Paper 1 Geographical Themes (1h45, 45%); Paper 2 Geographical Skills (1h30, 27.5%); Paper 3 Geographical Investigations (1h30, 27.5%).", "Three themes, maps, case studies and investigative skills; administrative zones 3 and 4 only.")
  };
  public static Source byCode(String code) {
    for(Source s:SOURCES)if(s.code.equals(code))return s;
    return null;
  }
  public static String referencesInfo(Source s){
    if(s==null)return "Reference not available.";
    return "Cambridge O Level syllabus "+s.code+" (exams "+s.validYears+").\n"+
      "2026 ASSESSMENT: "+s.examPapers+"\n"+
      "SPECIAL CONDITIONS: "+s.notes+"\n"+
      "OFFICIAL PDF: "+s.pdfUrl;
  }
}
