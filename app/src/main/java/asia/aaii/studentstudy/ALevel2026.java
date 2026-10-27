package asia.aaii.studentstudy;

/**
 * 2026 examination-year Cambridge AS/A Level study index.
 * Top-level module overview informed by each subject's official 2026 PDF.
 * Subtopic breakdown, lesson stages and exercises are ORIGINAL learning aids, NOT
 * a verified clause-by-clause transcription or complete endorsed Cambridge course.
 * Option/route modules must not be treated as all mandatory.
 */
public final class ALevel2026 {
  private ALevel2026(){}
  public static final String[] LESSON_STAGES={"Concept & Definitions","Worked Examples","Exam Skills & Review"};
  public static final class Module {
    public final String level,paper,name;
    public final String[] subtopics;
    public Module(String lev,String p,String n,String[] subs){level=lev;paper=p;name=n;subtopics=subs;}
  }
  public static final class Subject {
    public final String name,code,years,pdfUrl,pageUrl,examNotes;
    public final Module[] modules;
    public Subject(String n,String c,String y,String pdf,String page,String notes,Module[] m){
      name=n;code=c;years=y;pdfUrl=pdf;pageUrl=page;examNotes=notes;modules=m;
    }
    public int moduleCount(String mode){
      int count=0;for(Module m:modules)if(show(m,mode))count++;return count;
    }
    public int studyCount(String mode){
      int count=0;for(Module m:modules)if(show(m,mode))count+=m.subtopics.length;return count;
    }
    public boolean show(Module m,String mode){
      return "ALL".equals(mode)||"AS".equals(mode)&&"AS".equals(m.level)||
        "A".equals(mode)&&("AS".equals(m.level)||"A".equals(m.level)||"PRACTICAL".equals(m.level))||
        "OPTIONS".equals(mode)&&"OPTION".equals(m.level)||
        "ROUTES".equals(mode)&&"ROUTE".equals(m.level);
    }
  }
  public static final Subject[] SUBJECTS={
      new Subject("English Language", "9093", "2024–2026", "https://www.cambridgeinternational.org/Images/635901-2024-2026-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-english-language-9093/", "2026 Paper 1 Reading; Paper 2 Writing; Paper 3 Language Analysis; Paper 4 Language Topics. Do NOT use announced 2029 Paper 1 Text Analysis or new compulsory argumentative task for the 2026 examination.",new Module[]{
        new Module("AS", "Paper 1", "Reading",["text analysis","language, form and structure","audience, purpose and context","comparison and written commentary"]),
        new Module("AS", "Paper 2", "Writing",["imaginative writing","descriptive and narrative writing","discursive and persuasive writing","reflective commentary"]),
        new Module("A", "Paper 3", "Language analysis",["spoken language","language variation","language change and discourse analysis","transcription methods"]),
        new Module("A", "Paper 4", "Language topics",["English in the world","language and the self","child language acquisition","language change"])
      }),
      new Subject("Mathematics", "9709", "2026–2027", "https://www.cambridgeinternational.org/Images/697427-2026-2027-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-mathematics-9709/", "Paper 1 Pure 1 compulsory; AS/A paper combinations depend on route. Pure+Statistics route or Pure+Mechanics+Statistics route: verify entry combination in 2026 PDF.",new Module[]{
        new Module("AS", "Paper 1", "Pure Mathematics 1",["quadratics","functions","coordinate geometry","circular measure","trigonometry","series","differentiation","integration"]),
        new Module("AS", "Paper 2", "Pure Mathematics 2",["algebra","logarithmic and exponential functions","trigonometry","differentiation","integration","numerical solution"]),
        new Module("A", "Paper 3", "Pure Mathematics 3",["algebra","exponential and logarithmic functions","trigonometry","differentiation","integration","vectors","complex numbers","differential equations"]),
        new Module("ROUTE", "Paper 4", "Mechanics",["forces and equilibrium","kinematics of linear motion","momentum","Newton laws of motion","energy, work and power"]),
        new Module("ROUTE", "Paper 5", "Probability & Statistics 1",["representation of data","permutations and combinations","probability","discrete random variables","normal distribution"]),
        new Module("ROUTE", "Paper 6", "Probability & Statistics 2",["Poisson distribution","linear combinations of random variables","continuous random variables","sampling and estimation","hypothesis tests"])
      }),
      new Subject("Further Mathematics", "9231", "2026–2027", "https://www.cambridgeinternational.org/Images/697357-2026-2027-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-mathematics-further-9231/", "Paper 1 Further Pure Mathematics 1; remaining paper combinations depend on AS vs A entry route. Verify the 2026 PDF and required 9709 prior knowledge.",new Module[]{
        new Module("AS", "Paper 1", "Further Pure Mathematics 1",["roots of polynomial equations","rational functions and graphs","summation of series","matrices","polar coordinates","vectors","proof by induction"]),
        new Module("A", "Paper 2", "Further Pure Mathematics 2",["hyperbolic functions","matrices","differentiation","integration","complex numbers","differential equations"]),
        new Module("ROUTE", "Paper 3", "Further Mechanics",["projectile motion","equilibrium of a rigid body","circular motion","Hooke's law","variable force","momentum"]),
        new Module("ROUTE", "Paper 4", "Further Probability & Statistics",["continuous random variables","inference using normal and t-distributions","chi-squared tests","non-parametric tests","probability generating functions"])
      }),
      new Subject("Biology", "9700", "2025–2027", "https://www.cambridgeinternational.org/Images/664560-2025-2027-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-biology-9700/", "AS Papers 1,2,3 (including advanced practical skills), full A also Papers 4,5 (planning, analysis and evaluation). Science practical competence cannot be fully assessed by prompts alone.",new Module[]{
        new Module("AS", "AS Content", "Cell structure",["cell structure definitions and principles","cell structure applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Biological molecules",["biological molecules definitions and principles","biological molecules applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Enzymes",["enzymes definitions and principles","enzymes applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Cell membranes and transport",["cell membranes and transport definitions and principles","cell membranes and transport applications","data and original exam questions"]),
        new Module("AS", "AS Content", "The mitotic cell cycle",["the mitotic cell cycle definitions and principles","the mitotic cell cycle applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Nucleic acids and protein synthesis",["nucleic acids and protein synthesis definitions and principles","nucleic acids and protein synthesis applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Transport in plants",["transport in plants definitions and principles","transport in plants applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Transport in mammals",["transport in mammals definitions and principles","transport in mammals applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Gas exchange",["gas exchange definitions and principles","gas exchange applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Infectious diseases",["infectious diseases definitions and principles","infectious diseases applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Immunity",["immunity definitions and principles","immunity applications","data and original exam questions"]),
        new Module("A", "A Content", "Energy and respiration",["energy and respiration definitions and principles","energy and respiration applications","data and original exam questions"]),
        new Module("A", "A Content", "Photosynthesis",["photosynthesis definitions and principles","photosynthesis applications","data and original exam questions"]),
        new Module("A", "A Content", "Homeostasis",["homeostasis definitions and principles","homeostasis applications","data and original exam questions"]),
        new Module("A", "A Content", "Control and coordination",["control and coordination definitions and principles","control and coordination applications","data and original exam questions"]),
        new Module("A", "A Content", "Inheritance",["inheritance definitions and principles","inheritance applications","data and original exam questions"]),
        new Module("A", "A Content", "Selection and evolution",["selection and evolution definitions and principles","selection and evolution applications","data and original exam questions"]),
        new Module("A", "A Content", "Classification, biodiversity and conservation",["classification, biodiversity and conservation definitions and principles","classification, biodiversity and conservation applications","data and original exam questions"]),
        new Module("A", "A Content", "Genetic technology",["genetic technology definitions and principles","genetic technology applications","data and original exam questions"]),
        new Module("PRACTICAL", "Papers 3,5", "Practical, planning and data skills",["microscopy and lab apparatus","variables and reliable measurements","experimental planning","data analysis and evaluation"])
      }),
      new Subject("Chemistry", "9701", "2025–2027", "https://www.cambridgeinternational.org/Images/664563-2025-2027-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-chemistry-9701/", "AS Papers 1,2,3; A Papers 4,5 additionally. 2026 Paper 1 MCQ and advanced practical; do not claim paper practice substitutes for laboratory assessment.",new Module[]{
        new Module("AS", "AS Content", "Atomic structure",["atomic structure definitions and principles","atomic structure applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Atoms, molecules and stoichiometry",["atoms, molecules and stoichiometry definitions and principles","atoms, molecules and stoichiometry applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Chemical bonding",["chemical bonding definitions and principles","chemical bonding applications","data and original exam questions"]),
        new Module("AS", "AS Content", "States of matter",["states of matter definitions and principles","states of matter applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Chemical energetics",["chemical energetics definitions and principles","chemical energetics applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Electrochemistry",["electrochemistry definitions and principles","electrochemistry applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Equilibria",["equilibria definitions and principles","equilibria applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Reaction kinetics",["reaction kinetics definitions and principles","reaction kinetics applications","data and original exam questions"]),
        new Module("AS", "AS Content", "The Periodic Table: chemical periodicity",["the periodic table: chemical periodicity definitions and principles","the periodic table: chemical periodicity applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Group 2",["group 2 definitions and principles","group 2 applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Group 17",["group 17 definitions and principles","group 17 applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Nitrogen and sulfur",["nitrogen and sulfur definitions and principles","nitrogen and sulfur applications","data and original exam questions"]),
        new Module("AS", "AS Content", "An introduction to AS Level organic chemistry",["an introduction to as level organic chemistry definitions and principles","an introduction to as level organic chemistry applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Hydrocarbons",["hydrocarbons definitions and principles","hydrocarbons applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Halogen compounds",["halogen compounds definitions and principles","halogen compounds applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Hydroxy compounds",["hydroxy compounds definitions and principles","hydroxy compounds applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Carbonyl compounds",["carbonyl compounds definitions and principles","carbonyl compounds applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Carboxylic acids and derivatives",["carboxylic acids and derivatives definitions and principles","carboxylic acids and derivatives applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Nitrogen compounds",["nitrogen compounds definitions and principles","nitrogen compounds applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Polymerisation",["polymerisation definitions and principles","polymerisation applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Organic synthesis",["organic synthesis definitions and principles","organic synthesis applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Analytical techniques",["analytical techniques definitions and principles","analytical techniques applications","data and original exam questions"]),
        new Module("A", "A Content", "Chemical energetics — advanced",["chemical energetics — advanced definitions and principles","chemical energetics — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Electrochemistry — advanced",["electrochemistry — advanced definitions and principles","electrochemistry — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Equilibria — advanced",["equilibria — advanced definitions and principles","equilibria — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Reaction kinetics — advanced",["reaction kinetics — advanced definitions and principles","reaction kinetics — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Group 2 — advanced",["group 2 — advanced definitions and principles","group 2 — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Chemistry of transition elements",["chemistry of transition elements definitions and principles","chemistry of transition elements applications","data and original exam questions"]),
        new Module("A", "A Content", "An introduction to A Level organic chemistry",["an introduction to a level organic chemistry definitions and principles","an introduction to a level organic chemistry applications","data and original exam questions"]),
        new Module("A", "A Content", "Hydrocarbons — advanced",["hydrocarbons — advanced definitions and principles","hydrocarbons — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Halogen compounds — advanced",["halogen compounds — advanced definitions and principles","halogen compounds — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Hydroxy compounds — advanced",["hydroxy compounds — advanced definitions and principles","hydroxy compounds — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Carboxylic acids and derivatives — advanced",["carboxylic acids and derivatives — advanced definitions and principles","carboxylic acids and derivatives — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Nitrogen compounds — advanced",["nitrogen compounds — advanced definitions and principles","nitrogen compounds — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Polymerisation — advanced",["polymerisation — advanced definitions and principles","polymerisation — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Organic synthesis — advanced",["organic synthesis — advanced definitions and principles","organic synthesis — advanced applications","data and original exam questions"]),
        new Module("A", "A Content", "Analytical techniques — advanced",["analytical techniques — advanced definitions and principles","analytical techniques — advanced applications","data and original exam questions"]),
        new Module("PRACTICAL", "Papers 3,5", "Practical planning and evaluation",["qualitative analysis","titrations","rates and energetics experiments","data uncertainty and sources of error"])
      }),
      new Subject("Physics", "9702", "2025–2027", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-physics-9702/", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-physics-9702/", "AS Papers 1 MCQ, 2 Structured Questions and 3 Advanced Practical; full A additional Paper 4 Structured Questions and Paper 5 Planning, Analysis and Evaluation. PDF subject link opens official subject page.",new Module[]{
        new Module("AS", "AS Content", "Physical quantities and units",["physical quantities and units definitions and principles","physical quantities and units applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Kinematics",["kinematics definitions and principles","kinematics applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Dynamics",["dynamics definitions and principles","dynamics applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Forces, density and pressure",["forces, density and pressure definitions and principles","forces, density and pressure applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Work, energy and power",["work, energy and power definitions and principles","work, energy and power applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Deformation of solids",["deformation of solids definitions and principles","deformation of solids applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Waves",["waves definitions and principles","waves applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Superposition",["superposition definitions and principles","superposition applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Electricity",["electricity definitions and principles","electricity applications","data and original exam questions"]),
        new Module("AS", "AS Content", "D.C. circuits",["d.c. circuits definitions and principles","d.c. circuits applications","data and original exam questions"]),
        new Module("AS", "AS Content", "Particle physics",["particle physics definitions and principles","particle physics applications","data and original exam questions"]),
        new Module("A", "A Content", "Motion in a circle",["motion in a circle definitions and principles","motion in a circle applications","data and original exam questions"]),
        new Module("A", "A Content", "Gravitational fields",["gravitational fields definitions and principles","gravitational fields applications","data and original exam questions"]),
        new Module("A", "A Content", "Temperature",["temperature definitions and principles","temperature applications","data and original exam questions"]),
        new Module("A", "A Content", "Ideal gases",["ideal gases definitions and principles","ideal gases applications","data and original exam questions"]),
        new Module("A", "A Content", "Thermodynamics",["thermodynamics definitions and principles","thermodynamics applications","data and original exam questions"]),
        new Module("A", "A Content", "Oscillations",["oscillations definitions and principles","oscillations applications","data and original exam questions"]),
        new Module("A", "A Content", "Electric fields",["electric fields definitions and principles","electric fields applications","data and original exam questions"]),
        new Module("A", "A Content", "Capacitance",["capacitance definitions and principles","capacitance applications","data and original exam questions"]),
        new Module("A", "A Content", "Magnetic fields",["magnetic fields definitions and principles","magnetic fields applications","data and original exam questions"]),
        new Module("A", "A Content", "Alternating currents",["alternating currents definitions and principles","alternating currents applications","data and original exam questions"]),
        new Module("A", "A Content", "Quantum physics",["quantum physics definitions and principles","quantum physics applications","data and original exam questions"]),
        new Module("A", "A Content", "Nuclear physics",["nuclear physics definitions and principles","nuclear physics applications","data and original exam questions"]),
        new Module("A", "A Content", "Medical physics",["medical physics definitions and principles","medical physics applications","data and original exam questions"]),
        new Module("A", "A Content", "Astronomy and cosmology",["astronomy and cosmology definitions and principles","astronomy and cosmology applications","data and original exam questions"]),
        new Module("PRACTICAL", "Papers 3,5", "Practical and measurement skills",["uncertainty and significant figures","planning controlled experiments","graphs, gradients and units","evaluating method reliability"])
      }),
      new Subject("Computer Science", "9618", "2026", "https://www.cambridgeinternational.org/Images/697372-2026-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-computer-science-9618/", "2026 ONLY, not 2027-2029. Paper 1 Theory Fundamentals and Paper 2 Fundamental Problem-solving and Programming; A Papers 3 Advanced Theory, 4 Practical. Practical Paper 4 programming assessed using a computer.",new Module[]{
        new Module("AS", "Paper 1", "Information representation",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 1", "Communication",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 1", "Hardware",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 1", "Processor fundamentals",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 1", "System software",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 1", "Security, privacy and data integrity",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 1", "Ethics and ownership",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 1", "Databases",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 2", "Algorithm design and problem-solving",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 2", "Data types and structures",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 2", "Programming",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("AS", "Paper 2", "Software development",["key concepts and representation","real-world applications","worked programming/data tasks"]),
        new Module("A", "Paper 3", "Data representation",["advanced concepts","analysis or programming application","original practical and exam-style tasks"]),
        new Module("A", "Paper 3", "Communication and internet technologies",["advanced concepts","analysis or programming application","original practical and exam-style tasks"]),
        new Module("A", "Paper 3", "Hardware and virtual machines",["advanced concepts","analysis or programming application","original practical and exam-style tasks"]),
        new Module("A", "Paper 3", "System software",["advanced concepts","analysis or programming application","original practical and exam-style tasks"]),
        new Module("A", "Paper 3", "Security",["advanced concepts","analysis or programming application","original practical and exam-style tasks"]),
        new Module("A", "Paper 3", "Artificial intelligence",["advanced concepts","analysis or programming application","original practical and exam-style tasks"]),
        new Module("A", "Paper 4", "Computational thinking and problem-solving",["advanced concepts","analysis or programming application","original practical and exam-style tasks"]),
        new Module("A", "Paper 4", "Further programming",["advanced concepts","analysis or programming application","original practical and exam-style tasks"]),
        new Module("PRACTICAL", "Paper 4", "Advanced programming practical",["programming design and testing","pseudocode and algorithm tracing","file handling","testing and evaluation"])
      }),
      new Subject("Business", "9609", "2026–2028", "https://www.cambridgeinternational.org/Images/697371-2026-2028-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-business-9609/", "AS Papers 1 Business Concepts 1 and 2 Business Concepts 2; A Paper 3 Business Decision-Making and Paper 4 Business Strategy. A Level business strategy embedded across advanced content.",new Module[]{
        new Module("AS", "AS Content", "Business and its environment",["enterprise and objectives","business ownership and structure","business size and growth","external environment"]),
        new Module("AS", "AS Content", "Human resource management",["people in organisations","motivation","management and leadership","human resource management"]),
        new Module("AS", "AS Content", "Marketing",["marketing and markets","market research","marketing mix","strategy"]),
        new Module("AS", "AS Content", "Operations management",["nature of operations","inventory management","capacity and outsourcing","quality"]),
        new Module("AS", "AS Content", "Finance and accounting",["finance needs and sources","costs and profit","cash flow","financial accounting"]),
        new Module("A", "A Content", "Business and its environment — advanced",["external influences","strategic decisions","business growth and change"]),
        new Module("A", "A Content", "Human resource management — advanced",["organisational structure","communication","strategic human resources"]),
        new Module("A", "A Content", "Marketing — advanced",["strategic marketing","international marketing","product portfolio and marketing analysis"]),
        new Module("A", "A Content", "Operations management — advanced",["production planning","project management","lean production and quality"]),
        new Module("A", "A Content", "Finance and accounting — advanced",["published accounts","investment appraisal","strategic finance decisions"])
      }),
      new Subject("Economics", "9708", "2026–2028", "https://www.cambridgeinternational.org/Images/697423-2026-2028-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-economics-9708/", "AS content six main groups, A additional five groups. Papers 2 and 4 contain distinct microeconomics and macroeconomics essay sections; each has 2h duration.",new Module[]{
        new Module("AS", "AS Content", "Basic economic ideas and resource allocation",["scarcity choice opportunity cost","economic methodology","factors of production","different economic systems","production possibility curves","goods and services"]),
        new Module("AS", "AS Content", "The price system and the microeconomy",["demand and supply","elasticities","price equilibrium","consumer and producer surplus"]),
        new Module("AS", "AS Content", "Government microeconomic intervention",["government intervention","taxes and subsidies","income and wealth inequality"]),
        new Module("AS", "AS Content", "The macroeconomy",["national income statistics","circular flow","aggregate demand and supply","economic growth","unemployment","price stability"]),
        new Module("AS", "AS Content", "Government macroeconomic intervention",["macroeconomic policy objectives","fiscal and monetary policies","supply-side policies"]),
        new Module("AS", "AS Content", "International economic issues",["international trade","exchange rates","terms of trade","balance of payments"]),
        new Module("A", "A Content", "The price system and the microeconomy — advanced",["utility theory","indifference curves","market failure","externalities","costs and profits","market structures","firm objectives"]),
        new Module("A", "A Content", "Government microeconomic intervention — advanced",["efficiency and market failure","equity and redistribution","labour market intervention"]),
        new Module("A", "A Content", "The macroeconomy — advanced",["circular flow and multipliers","economic growth and sustainability","unemployment","money and banking"]),
        new Module("A", "A Content", "Government macroeconomic intervention — advanced",["macroeconomic policy evaluation","government debt","inflation policy"]),
        new Module("A", "A Content", "International economic issues — advanced",["protectionism","exchange rate systems","economic integration","development and globalisation"])
      }),
      new Subject("Accounting", "9706", "2026–2028", "https://www.cambridgeinternational.org/Images/697417-2026-2028-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-accounting-9706/", "AS Paper 1 Multiple Choice & Paper 2 Fundamentals of Accounting; full A Paper 3 Financial Accounting & Paper 4 Cost and Management Accounting.",new Module[]{
        new Module("AS", "Papers 1–2", "Financial accounting — AS",["types of business entity","accounting system","non-current assets","reconciliation and verification","preparation of financial statements","analysis and communication"]),
        new Module("AS", "Papers 1–2", "Cost and management accounting — AS",["costs and cost behaviour","traditional costing methods"]),
        new Module("A", "Paper 3", "Financial accounting — A",["financial statements","regulatory and ethical considerations","business acquisition and merger","computerised accounting systems","analysis of accounting information"]),
        new Module("A", "Paper 4", "Cost and management accounting — A",["activity-based costing","standard costing","budgeting and budgetary control","investment appraisal"])
      }),
      new Subject("History", "9489", "2026", "https://www.cambridgeinternational.org/Images/697368-2026-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-history-9489/", "AS Papers 1 & 2 choose ONE European/American/International option. A Paper 3: choose topic as per exam entry; Paper 4 depth study also optional by route. 2026 June/November only (no March series). Never make all routes compulsory.",new Module[]{
        new Module("OPTION", "AS P1–P2", "AS European option — Modern Europe 1750–1921",["France 1774–1814","Industrial Revolution Britain 1750–1850","Germany liberalism and nationalism 1815–71","Russian Revolution 1894–1921"]),
        new Module("OPTION", "AS P1–P2", "AS American option — USA 1820–1941",["origins of Civil War 1820–61","Civil War and Reconstruction","Gilded Age and Progressive Era","Great Crash, Depression and New Deal"]),
        new Module("OPTION", "AS P1–P2", "AS International option — 1870–1945",["emergence of world powers","League of Nations in 1920s","League of Nations in 1930s","China and Japan 1912–45"]),
        new Module("A", "Paper 3", "Interpretations / Depth: The origins of WWI",["interpretations of war origins","source analysis","historical argument"]),
        new Module("A", "Paper 3", "Interpretations / Depth: The Holocaust",["historical evidence","interpretations","responsible argumentation"]),
        new Module("A", "Paper 3", "Interpretations / Depth: Cold War",["origins and development","historical debate","interpretations"]),
        new Module("OPTION", "Paper 4", "European depth option",["Mussolini's Italy","Stalin's Russia","Hitler's Germany","Britain 1919–39"]),
        new Module("OPTION", "Paper 4", "American depth option",["USA in 1940s and 1950s","USA in 1960s and 1970s","USA in 1980s","foreign policy 1944–92"]),
        new Module("OPTION", "Paper 4", "International depth option",["international crises and political developments","historical investigations","document interpretation"])
      }),
      new Subject("Psychology", "9990", "2024–2026", "https://www.cambridgeinternational.org/Images/634461-2024-2026-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-psychology-9990/", "AS four core approaches, 12 core studies, research methods. A Level requires AS content PLUS TWO of four specialist options: Clinical, Consumer, Health, Organisational. Verify named core studies against 2026 reference list, not 2027 set.",new Module[]{
        new Module("AS", "Papers 1–2", "Biological approach",["biological explanations of behaviour","core studies evidence","research evaluation"]),
        new Module("AS", "Papers 1–2", "Cognitive approach",["memory and thinking","cognitive core studies","methods and ethics"]),
        new Module("AS", "Papers 1–2", "Learning approach",["classical conditioning","operant conditioning","learning evidence"]),
        new Module("AS", "Papers 1–2", "Social approach",["social influence","social core studies","evaluation and application"]),
        new Module("AS", "Paper 2", "Research methodology",["experiments","self-report and observation","sampling and ethics","research methods and data"]),
        new Module("OPTION", "Papers 3–4", "Clinical Psychology",["diagnosis and symptoms","explanations","therapeutic approaches","methods and debates"]),
        new Module("OPTION", "Papers 3–4", "Consumer Psychology",["consumer behaviour","retail and purchasing decisions","advertising","research applications"]),
        new Module("OPTION", "Papers 3–4", "Health Psychology",["health behaviour","stress and pain","adherence and health choices","interventions"]),
        new Module("OPTION", "Papers 3–4", "Organisational Psychology",["workplace behaviour","motivation and leadership","teams and communication","research methods"])
      })
  };
  public static Subject byCode(String code){for(Subject s:SUBJECTS)if(s.code.equals(code))return s;return null;}
  public static int moduleCount(){int n=0;for(Subject s:SUBJECTS)n+=s.modules.length;return n;}
  public static int subtopicCount(){int n=0;for(Subject s:SUBJECTS)for(Module m:s.modules)n+=m.subtopics.length;return n;}
}
