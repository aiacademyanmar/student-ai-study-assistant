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
        new Module("AS", "Paper 1", "Reading",new String[]{"text analysis","language, form and structure","audience, purpose and context","comparison and written commentary"}),
        new Module("AS", "Paper 2", "Writing",new String[]{"imaginative writing","descriptive and narrative writing","discursive and persuasive writing","reflective commentary"}),
        new Module("A", "Paper 3", "Language analysis",new String[]{"spoken language","language variation","language change and discourse analysis","transcription methods"}),
        new Module("A", "Paper 4", "Language topics",new String[]{"English in the world","language and the self","child language acquisition","language change"})
      }),
      new Subject("Mathematics", "9709", "2026–2027", "https://www.cambridgeinternational.org/Images/697427-2026-2027-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-mathematics-9709/", "Paper 1 Pure 1 compulsory; AS/A paper combinations depend on route. Pure+Statistics route or Pure+Mechanics+Statistics route: verify entry combination in 2026 PDF.",new Module[]{
        new Module("AS", "Paper 1", "Pure Mathematics 1",new String[]{"quadratics","functions","coordinate geometry","circular measure","trigonometry","series","differentiation","integration"}),
        new Module("AS", "Paper 2", "Pure Mathematics 2",new String[]{"algebra","logarithmic and exponential functions","trigonometry","differentiation","integration","numerical solution"}),
        new Module("A", "Paper 3", "Pure Mathematics 3",new String[]{"algebra","exponential and logarithmic functions","trigonometry","differentiation","integration","vectors","complex numbers","differential equations"}),
        new Module("ROUTE", "Paper 4", "Mechanics",new String[]{"forces and equilibrium","kinematics of linear motion","momentum","Newton laws of motion","energy, work and power"}),
        new Module("ROUTE", "Paper 5", "Probability & Statistics 1",new String[]{"representation of data","permutations and combinations","probability","discrete random variables","normal distribution"}),
        new Module("ROUTE", "Paper 6", "Probability & Statistics 2",new String[]{"Poisson distribution","linear combinations of random variables","continuous random variables","sampling and estimation","hypothesis tests"})
      }),
      new Subject("Further Mathematics", "9231", "2026–2027", "https://www.cambridgeinternational.org/Images/697357-2026-2027-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-mathematics-further-9231/", "Paper 1 Further Pure Mathematics 1; remaining paper combinations depend on AS vs A entry route. Verify the 2026 PDF and required 9709 prior knowledge.",new Module[]{
        new Module("AS", "Paper 1", "Further Pure Mathematics 1",new String[]{"roots of polynomial equations","rational functions and graphs","summation of series","matrices","polar coordinates","vectors","proof by induction"}),
        new Module("A", "Paper 2", "Further Pure Mathematics 2",new String[]{"hyperbolic functions","matrices","differentiation","integration","complex numbers","differential equations"}),
        new Module("ROUTE", "Paper 3", "Further Mechanics",new String[]{"projectile motion","equilibrium of a rigid body","circular motion","Hooke's law","variable force","momentum"}),
        new Module("ROUTE", "Paper 4", "Further Probability & Statistics",new String[]{"continuous random variables","inference using normal and t-distributions","chi-squared tests","non-parametric tests","probability generating functions"})
      }),
      new Subject("Biology", "9700", "2025–2027", "https://www.cambridgeinternational.org/Images/664560-2025-2027-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-biology-9700/", "AS Papers 1,2,3 (including advanced practical skills), full A also Papers 4,5 (planning, analysis and evaluation). Science practical competence cannot be fully assessed by prompts alone.",new Module[]{
        new Module("AS", "AS Content", "Cell structure",new String[]{"cell structure definitions and principles","cell structure applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Biological molecules",new String[]{"biological molecules definitions and principles","biological molecules applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Enzymes",new String[]{"enzymes definitions and principles","enzymes applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Cell membranes and transport",new String[]{"cell membranes and transport definitions and principles","cell membranes and transport applications","data and original exam questions"}),
        new Module("AS", "AS Content", "The mitotic cell cycle",new String[]{"the mitotic cell cycle definitions and principles","the mitotic cell cycle applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Nucleic acids and protein synthesis",new String[]{"nucleic acids and protein synthesis definitions and principles","nucleic acids and protein synthesis applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Transport in plants",new String[]{"transport in plants definitions and principles","transport in plants applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Transport in mammals",new String[]{"transport in mammals definitions and principles","transport in mammals applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Gas exchange",new String[]{"gas exchange definitions and principles","gas exchange applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Infectious diseases",new String[]{"infectious diseases definitions and principles","infectious diseases applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Immunity",new String[]{"immunity definitions and principles","immunity applications","data and original exam questions"}),
        new Module("A", "A Content", "Energy and respiration",new String[]{"energy and respiration definitions and principles","energy and respiration applications","data and original exam questions"}),
        new Module("A", "A Content", "Photosynthesis",new String[]{"photosynthesis definitions and principles","photosynthesis applications","data and original exam questions"}),
        new Module("A", "A Content", "Homeostasis",new String[]{"homeostasis definitions and principles","homeostasis applications","data and original exam questions"}),
        new Module("A", "A Content", "Control and coordination",new String[]{"control and coordination definitions and principles","control and coordination applications","data and original exam questions"}),
        new Module("A", "A Content", "Inheritance",new String[]{"inheritance definitions and principles","inheritance applications","data and original exam questions"}),
        new Module("A", "A Content", "Selection and evolution",new String[]{"selection and evolution definitions and principles","selection and evolution applications","data and original exam questions"}),
        new Module("A", "A Content", "Classification, biodiversity and conservation",new String[]{"classification, biodiversity and conservation definitions and principles","classification, biodiversity and conservation applications","data and original exam questions"}),
        new Module("A", "A Content", "Genetic technology",new String[]{"genetic technology definitions and principles","genetic technology applications","data and original exam questions"}),
        new Module("PRACTICAL", "Papers 3,5", "Practical, planning and data skills",new String[]{"microscopy and lab apparatus","variables and reliable measurements","experimental planning","data analysis and evaluation"})
      }),
      new Subject("Chemistry", "9701", "2025–2027", "https://www.cambridgeinternational.org/Images/664563-2025-2027-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-chemistry-9701/", "AS Papers 1,2,3; A Papers 4,5 additionally. 2026 Paper 1 MCQ and advanced practical; do not claim paper practice substitutes for laboratory assessment.",new Module[]{
        new Module("AS", "AS Content", "Atomic structure",new String[]{"atomic structure definitions and principles","atomic structure applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Atoms, molecules and stoichiometry",new String[]{"atoms, molecules and stoichiometry definitions and principles","atoms, molecules and stoichiometry applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Chemical bonding",new String[]{"chemical bonding definitions and principles","chemical bonding applications","data and original exam questions"}),
        new Module("AS", "AS Content", "States of matter",new String[]{"states of matter definitions and principles","states of matter applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Chemical energetics",new String[]{"chemical energetics definitions and principles","chemical energetics applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Electrochemistry",new String[]{"electrochemistry definitions and principles","electrochemistry applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Equilibria",new String[]{"equilibria definitions and principles","equilibria applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Reaction kinetics",new String[]{"reaction kinetics definitions and principles","reaction kinetics applications","data and original exam questions"}),
        new Module("AS", "AS Content", "The Periodic Table: chemical periodicity",new String[]{"the periodic table: chemical periodicity definitions and principles","the periodic table: chemical periodicity applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Group 2",new String[]{"group 2 definitions and principles","group 2 applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Group 17",new String[]{"group 17 definitions and principles","group 17 applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Nitrogen and sulfur",new String[]{"nitrogen and sulfur definitions and principles","nitrogen and sulfur applications","data and original exam questions"}),
        new Module("AS", "AS Content", "An introduction to AS Level organic chemistry",new String[]{"an introduction to as level organic chemistry definitions and principles","an introduction to as level organic chemistry applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Hydrocarbons",new String[]{"hydrocarbons definitions and principles","hydrocarbons applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Halogen compounds",new String[]{"halogen compounds definitions and principles","halogen compounds applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Hydroxy compounds",new String[]{"hydroxy compounds definitions and principles","hydroxy compounds applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Carbonyl compounds",new String[]{"carbonyl compounds definitions and principles","carbonyl compounds applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Carboxylic acids and derivatives",new String[]{"carboxylic acids and derivatives definitions and principles","carboxylic acids and derivatives applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Nitrogen compounds",new String[]{"nitrogen compounds definitions and principles","nitrogen compounds applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Polymerisation",new String[]{"polymerisation definitions and principles","polymerisation applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Organic synthesis",new String[]{"organic synthesis definitions and principles","organic synthesis applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Analytical techniques",new String[]{"analytical techniques definitions and principles","analytical techniques applications","data and original exam questions"}),
        new Module("A", "A Content", "Chemical energetics — advanced",new String[]{"chemical energetics — advanced definitions and principles","chemical energetics — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Electrochemistry — advanced",new String[]{"electrochemistry — advanced definitions and principles","electrochemistry — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Equilibria — advanced",new String[]{"equilibria — advanced definitions and principles","equilibria — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Reaction kinetics — advanced",new String[]{"reaction kinetics — advanced definitions and principles","reaction kinetics — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Group 2 — advanced",new String[]{"group 2 — advanced definitions and principles","group 2 — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Chemistry of transition elements",new String[]{"chemistry of transition elements definitions and principles","chemistry of transition elements applications","data and original exam questions"}),
        new Module("A", "A Content", "An introduction to A Level organic chemistry",new String[]{"an introduction to a level organic chemistry definitions and principles","an introduction to a level organic chemistry applications","data and original exam questions"}),
        new Module("A", "A Content", "Hydrocarbons — advanced",new String[]{"hydrocarbons — advanced definitions and principles","hydrocarbons — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Halogen compounds — advanced",new String[]{"halogen compounds — advanced definitions and principles","halogen compounds — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Hydroxy compounds — advanced",new String[]{"hydroxy compounds — advanced definitions and principles","hydroxy compounds — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Carboxylic acids and derivatives — advanced",new String[]{"carboxylic acids and derivatives — advanced definitions and principles","carboxylic acids and derivatives — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Nitrogen compounds — advanced",new String[]{"nitrogen compounds — advanced definitions and principles","nitrogen compounds — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Polymerisation — advanced",new String[]{"polymerisation — advanced definitions and principles","polymerisation — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Organic synthesis — advanced",new String[]{"organic synthesis — advanced definitions and principles","organic synthesis — advanced applications","data and original exam questions"}),
        new Module("A", "A Content", "Analytical techniques — advanced",new String[]{"analytical techniques — advanced definitions and principles","analytical techniques — advanced applications","data and original exam questions"}),
        new Module("PRACTICAL", "Papers 3,5", "Practical planning and evaluation",new String[]{"qualitative analysis","titrations","rates and energetics experiments","data uncertainty and sources of error"})
      }),
      new Subject("Physics", "9702", "2025–2027", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-physics-9702/", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-physics-9702/", "AS Papers 1 MCQ, 2 Structured Questions and 3 Advanced Practical; full A additional Paper 4 Structured Questions and Paper 5 Planning, Analysis and Evaluation. PDF subject link opens official subject page.",new Module[]{
        new Module("AS", "AS Content", "Physical quantities and units",new String[]{"physical quantities and units definitions and principles","physical quantities and units applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Kinematics",new String[]{"kinematics definitions and principles","kinematics applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Dynamics",new String[]{"dynamics definitions and principles","dynamics applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Forces, density and pressure",new String[]{"forces, density and pressure definitions and principles","forces, density and pressure applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Work, energy and power",new String[]{"work, energy and power definitions and principles","work, energy and power applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Deformation of solids",new String[]{"deformation of solids definitions and principles","deformation of solids applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Waves",new String[]{"waves definitions and principles","waves applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Superposition",new String[]{"superposition definitions and principles","superposition applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Electricity",new String[]{"electricity definitions and principles","electricity applications","data and original exam questions"}),
        new Module("AS", "AS Content", "D.C. circuits",new String[]{"d.c. circuits definitions and principles","d.c. circuits applications","data and original exam questions"}),
        new Module("AS", "AS Content", "Particle physics",new String[]{"particle physics definitions and principles","particle physics applications","data and original exam questions"}),
        new Module("A", "A Content", "Motion in a circle",new String[]{"motion in a circle definitions and principles","motion in a circle applications","data and original exam questions"}),
        new Module("A", "A Content", "Gravitational fields",new String[]{"gravitational fields definitions and principles","gravitational fields applications","data and original exam questions"}),
        new Module("A", "A Content", "Temperature",new String[]{"temperature definitions and principles","temperature applications","data and original exam questions"}),
        new Module("A", "A Content", "Ideal gases",new String[]{"ideal gases definitions and principles","ideal gases applications","data and original exam questions"}),
        new Module("A", "A Content", "Thermodynamics",new String[]{"thermodynamics definitions and principles","thermodynamics applications","data and original exam questions"}),
        new Module("A", "A Content", "Oscillations",new String[]{"oscillations definitions and principles","oscillations applications","data and original exam questions"}),
        new Module("A", "A Content", "Electric fields",new String[]{"electric fields definitions and principles","electric fields applications","data and original exam questions"}),
        new Module("A", "A Content", "Capacitance",new String[]{"capacitance definitions and principles","capacitance applications","data and original exam questions"}),
        new Module("A", "A Content", "Magnetic fields",new String[]{"magnetic fields definitions and principles","magnetic fields applications","data and original exam questions"}),
        new Module("A", "A Content", "Alternating currents",new String[]{"alternating currents definitions and principles","alternating currents applications","data and original exam questions"}),
        new Module("A", "A Content", "Quantum physics",new String[]{"quantum physics definitions and principles","quantum physics applications","data and original exam questions"}),
        new Module("A", "A Content", "Nuclear physics",new String[]{"nuclear physics definitions and principles","nuclear physics applications","data and original exam questions"}),
        new Module("A", "A Content", "Medical physics",new String[]{"medical physics definitions and principles","medical physics applications","data and original exam questions"}),
        new Module("A", "A Content", "Astronomy and cosmology",new String[]{"astronomy and cosmology definitions and principles","astronomy and cosmology applications","data and original exam questions"}),
        new Module("PRACTICAL", "Papers 3,5", "Practical and measurement skills",new String[]{"uncertainty and significant figures","planning controlled experiments","graphs, gradients and units","evaluating method reliability"})
      }),
      new Subject("Computer Science", "9618", "2026", "https://www.cambridgeinternational.org/Images/697372-2026-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-computer-science-9618/", "2026 ONLY, not 2027-2029. Paper 1 Theory Fundamentals and Paper 2 Fundamental Problem-solving and Programming; A Papers 3 Advanced Theory, 4 Practical. Practical Paper 4 programming assessed using a computer.",new Module[]{
        new Module("AS", "Paper 1", "Information representation",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 1", "Communication",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 1", "Hardware",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 1", "Processor fundamentals",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 1", "System software",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 1", "Security, privacy and data integrity",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 1", "Ethics and ownership",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 1", "Databases",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 2", "Algorithm design and problem-solving",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 2", "Data types and structures",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 2", "Programming",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("AS", "Paper 2", "Software development",new String[]{"key concepts and representation","real-world applications","worked programming/data tasks"}),
        new Module("A", "Paper 3", "Data representation",new String[]{"advanced concepts","analysis or programming application","original practical and exam-style tasks"}),
        new Module("A", "Paper 3", "Communication and internet technologies",new String[]{"advanced concepts","analysis or programming application","original practical and exam-style tasks"}),
        new Module("A", "Paper 3", "Hardware and virtual machines",new String[]{"advanced concepts","analysis or programming application","original practical and exam-style tasks"}),
        new Module("A", "Paper 3", "System software",new String[]{"advanced concepts","analysis or programming application","original practical and exam-style tasks"}),
        new Module("A", "Paper 3", "Security",new String[]{"advanced concepts","analysis or programming application","original practical and exam-style tasks"}),
        new Module("A", "Paper 3", "Artificial intelligence",new String[]{"advanced concepts","analysis or programming application","original practical and exam-style tasks"}),
        new Module("A", "Paper 4", "Computational thinking and problem-solving",new String[]{"advanced concepts","analysis or programming application","original practical and exam-style tasks"}),
        new Module("A", "Paper 4", "Further programming",new String[]{"advanced concepts","analysis or programming application","original practical and exam-style tasks"}),
        new Module("PRACTICAL", "Paper 4", "Advanced programming practical",new String[]{"programming design and testing","pseudocode and algorithm tracing","file handling","testing and evaluation"})
      }),
      new Subject("Business", "9609", "2026–2028", "https://www.cambridgeinternational.org/Images/697371-2026-2028-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-business-9609/", "AS Papers 1 Business Concepts 1 and 2 Business Concepts 2; A Paper 3 Business Decision-Making and Paper 4 Business Strategy. A Level business strategy embedded across advanced content.",new Module[]{
        new Module("AS", "AS Content", "Business and its environment",new String[]{"enterprise and objectives","business ownership and structure","business size and growth","external environment"}),
        new Module("AS", "AS Content", "Human resource management",new String[]{"people in organisations","motivation","management and leadership","human resource management"}),
        new Module("AS", "AS Content", "Marketing",new String[]{"marketing and markets","market research","marketing mix","strategy"}),
        new Module("AS", "AS Content", "Operations management",new String[]{"nature of operations","inventory management","capacity and outsourcing","quality"}),
        new Module("AS", "AS Content", "Finance and accounting",new String[]{"finance needs and sources","costs and profit","cash flow","financial accounting"}),
        new Module("A", "A Content", "Business and its environment — advanced",new String[]{"external influences","strategic decisions","business growth and change"}),
        new Module("A", "A Content", "Human resource management — advanced",new String[]{"organisational structure","communication","strategic human resources"}),
        new Module("A", "A Content", "Marketing — advanced",new String[]{"strategic marketing","international marketing","product portfolio and marketing analysis"}),
        new Module("A", "A Content", "Operations management — advanced",new String[]{"production planning","project management","lean production and quality"}),
        new Module("A", "A Content", "Finance and accounting — advanced",new String[]{"published accounts","investment appraisal","strategic finance decisions"})
      }),
      new Subject("Economics", "9708", "2026–2028", "https://www.cambridgeinternational.org/Images/697423-2026-2028-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-economics-9708/", "AS content six main groups, A additional five groups. Papers 2 and 4 contain distinct microeconomics and macroeconomics essay sections; each has 2h duration.",new Module[]{
        new Module("AS", "AS Content", "Basic economic ideas and resource allocation",new String[]{"scarcity choice opportunity cost","economic methodology","factors of production","different economic systems","production possibility curves","goods and services"}),
        new Module("AS", "AS Content", "The price system and the microeconomy",new String[]{"demand and supply","elasticities","price equilibrium","consumer and producer surplus"}),
        new Module("AS", "AS Content", "Government microeconomic intervention",new String[]{"government intervention","taxes and subsidies","income and wealth inequality"}),
        new Module("AS", "AS Content", "The macroeconomy",new String[]{"national income statistics","circular flow","aggregate demand and supply","economic growth","unemployment","price stability"}),
        new Module("AS", "AS Content", "Government macroeconomic intervention",new String[]{"macroeconomic policy objectives","fiscal and monetary policies","supply-side policies"}),
        new Module("AS", "AS Content", "International economic issues",new String[]{"international trade","exchange rates","terms of trade","balance of payments"}),
        new Module("A", "A Content", "The price system and the microeconomy — advanced",new String[]{"utility theory","indifference curves","market failure","externalities","costs and profits","market structures","firm objectives"}),
        new Module("A", "A Content", "Government microeconomic intervention — advanced",new String[]{"efficiency and market failure","equity and redistribution","labour market intervention"}),
        new Module("A", "A Content", "The macroeconomy — advanced",new String[]{"circular flow and multipliers","economic growth and sustainability","unemployment","money and banking"}),
        new Module("A", "A Content", "Government macroeconomic intervention — advanced",new String[]{"macroeconomic policy evaluation","government debt","inflation policy"}),
        new Module("A", "A Content", "International economic issues — advanced",new String[]{"protectionism","exchange rate systems","economic integration","development and globalisation"})
      }),
      new Subject("Accounting", "9706", "2026–2028", "https://www.cambridgeinternational.org/Images/697417-2026-2028-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-accounting-9706/", "AS Paper 1 Multiple Choice & Paper 2 Fundamentals of Accounting; full A Paper 3 Financial Accounting & Paper 4 Cost and Management Accounting.",new Module[]{
        new Module("AS", "Papers 1–2", "Financial accounting — AS",new String[]{"types of business entity","accounting system","non-current assets","reconciliation and verification","preparation of financial statements","analysis and communication"}),
        new Module("AS", "Papers 1–2", "Cost and management accounting — AS",new String[]{"costs and cost behaviour","traditional costing methods"}),
        new Module("A", "Paper 3", "Financial accounting — A",new String[]{"financial statements","regulatory and ethical considerations","business acquisition and merger","computerised accounting systems","analysis of accounting information"}),
        new Module("A", "Paper 4", "Cost and management accounting — A",new String[]{"activity-based costing","standard costing","budgeting and budgetary control","investment appraisal"})
      }),
      new Subject("History", "9489", "2026", "https://www.cambridgeinternational.org/Images/697368-2026-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-history-9489/", "AS Papers 1 & 2 choose ONE European/American/International option. A Paper 3: choose topic as per exam entry; Paper 4 depth study also optional by route. 2026 June/November only (no March series). Never make all routes compulsory.",new Module[]{
        new Module("OPTION", "AS P1–P2", "AS European option — Modern Europe 1750–1921",new String[]{"France 1774–1814","Industrial Revolution Britain 1750–1850","Germany liberalism and nationalism 1815–71","Russian Revolution 1894–1921"}),
        new Module("OPTION", "AS P1–P2", "AS American option — USA 1820–1941",new String[]{"origins of Civil War 1820–61","Civil War and Reconstruction","Gilded Age and Progressive Era","Great Crash, Depression and New Deal"}),
        new Module("OPTION", "AS P1–P2", "AS International option — 1870–1945",new String[]{"emergence of world powers","League of Nations in 1920s","League of Nations in 1930s","China and Japan 1912–45"}),
        new Module("A", "Paper 3", "Interpretations / Depth: The origins of WWI",new String[]{"interpretations of war origins","source analysis","historical argument"}),
        new Module("A", "Paper 3", "Interpretations / Depth: The Holocaust",new String[]{"historical evidence","interpretations","responsible argumentation"}),
        new Module("A", "Paper 3", "Interpretations / Depth: Cold War",new String[]{"origins and development","historical debate","interpretations"}),
        new Module("OPTION", "Paper 4", "European depth option",new String[]{"Mussolini's Italy","Stalin's Russia","Hitler's Germany","Britain 1919–39"}),
        new Module("OPTION", "Paper 4", "American depth option",new String[]{"USA in 1940s and 1950s","USA in 1960s and 1970s","USA in 1980s","foreign policy 1944–92"}),
        new Module("OPTION", "Paper 4", "International depth option",new String[]{"international crises and political developments","historical investigations","document interpretation"})
      }),
      new Subject("Psychology", "9990", "2024–2026", "https://www.cambridgeinternational.org/Images/634461-2024-2026-syllabus.pdf", "https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-international-as-and-a-level-psychology-9990/", "AS four core approaches, 12 core studies, research methods. A Level requires AS content PLUS TWO of four specialist options: Clinical, Consumer, Health, Organisational. Verify named core studies against 2026 reference list, not 2027 set.",new Module[]{
        new Module("AS", "Papers 1–2", "Biological approach",new String[]{"biological explanations of behaviour","core studies evidence","research evaluation"}),
        new Module("AS", "Papers 1–2", "Cognitive approach",new String[]{"memory and thinking","cognitive core studies","methods and ethics"}),
        new Module("AS", "Papers 1–2", "Learning approach",new String[]{"classical conditioning","operant conditioning","learning evidence"}),
        new Module("AS", "Papers 1–2", "Social approach",new String[]{"social influence","social core studies","evaluation and application"}),
        new Module("AS", "Paper 2", "Research methodology",new String[]{"experiments","self-report and observation","sampling and ethics","research methods and data"}),
        new Module("OPTION", "Papers 3–4", "Clinical Psychology",new String[]{"diagnosis and symptoms","explanations","therapeutic approaches","methods and debates"}),
        new Module("OPTION", "Papers 3–4", "Consumer Psychology",new String[]{"consumer behaviour","retail and purchasing decisions","advertising","research applications"}),
        new Module("OPTION", "Papers 3–4", "Health Psychology",new String[]{"health behaviour","stress and pain","adherence and health choices","interventions"}),
        new Module("OPTION", "Papers 3–4", "Organisational Psychology",new String[]{"workplace behaviour","motivation and leadership","teams and communication","research methods"})
      })
  };
  public static Subject byCode(String code){for(Subject s:SUBJECTS)if(s.code.equals(code))return s;return null;}
  public static int moduleCount(){int n=0;for(Subject s:SUBJECTS)n+=s.modules.length;return n;}
  public static int subtopicCount(){int n=0;for(Subject s:SUBJECTS)for(Module m:s.modules)n+=m.subtopics.length;return n;}
}
