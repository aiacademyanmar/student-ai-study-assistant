package asia.aaii.studentstudy;

/**
 * Original Cambridge-oriented GCE study maps. Chapters are starter learning groupings,
 * not exact reproduction of any exam board's full syllabus or official chapter ordering.
 */
public final class GceCurriculum {
    private GceCurriculum(){}
    public static final class Subject {
        public final String level, name, code;
        public final Chapter[] chapters;
        Subject(String level, String name, String code, Chapter[] chapters) {
            this.level=level;this.name=name;this.code=code;this.chapters=chapters;
        }
    }
    public static final class Chapter {
        public final String title, focus;
        Chapter(String title,String focus){this.title=title;this.focus=focus;}
    }
    public static final String[] LESSONS={
        "Lesson 1 · အခြေခံအယူအဆများ",
        "Lesson 2 · Worked Examples နှင့် နည်းလမ်းများ",
        "Lesson 3 · Exam Skills နှင့် အသုံးချခြင်း"
    };
    public static final String[] EXERCISES={
        "Concept Check · နားလည်မှုစစ်ဆေးရန်",
        "MCQ · ရွေးချယ်ဖြေဆိုခြင်း",
        "Structured Questions · အဆင့်လိုက်တွက်ချက်/ရေးသားခြင်း",
        "Error Spotting · အမှားရှာပြီးပြင်ခြင်း",
        "Timed Exam Practice · အချိန်သတ်မှတ်လေ့ကျင့်ခြင်း"
    };
    public static final Subject[] SUBJECTS={
        new Subject("O", "English Language", "1123", new Chapter[]{
            new Chapter("Reading Comprehension", "main idea, inference and supporting evidence"),
            new Chapter("Summary Writing", "selection, paraphrase and concision"),
            new Chapter("Directed Writing", "audience, purpose and appropriate register"),
            new Chapter("Composition", "planning, coherence and descriptive techniques")
        }),
        new Subject("O", "Mathematics (D)", "4024", new Chapter[]{
            new Chapter("Numbers and Ratio", "fractions, percentages, rates and proportion"),
            new Chapter("Algebra and Graphs", "linear equations, inequalities and coordinates"),
            new Chapter("Geometry and Mensuration", "angle rules, area, volume and similarity"),
            new Chapter("Statistics and Probability", "averages, data displays and probabilities")
        }),
        new Subject("O", "Additional Mathematics", "4037", new Chapter[]{
            new Chapter("Functions and Polynomials", "composite functions, remainder theorem and factorisation"),
            new Chapter("Trigonometry", "identities, graphs and trigonometric equations"),
            new Chapter("Differentiation", "gradients, tangents and stationary points"),
            new Chapter("Integration", "area under curves and anti-derivatives")
        }),
        new Subject("O", "Biology", "5090", new Chapter[]{
            new Chapter("Cells and Organisation", "cell structure, specialised cells and transport"),
            new Chapter("Nutrition and Enzymes", "photosynthesis, enzymes and human digestion"),
            new Chapter("Transport and Coordination", "circulatory system, plant transport and responses"),
            new Chapter("Inheritance and Ecology", "genetics, ecosystems and sustainability")
        }),
        new Subject("O", "Chemistry", "5070", new Chapter[]{
            new Chapter("Particles and Atomic Structure", "states of matter, atoms, bonding and formulae"),
            new Chapter("Stoichiometry", "moles, equations and quantitative calculations"),
            new Chapter("Chemical Reactions", "acids, salts, redox and energetic changes"),
            new Chapter("Organic and Environmental Chemistry", "hydrocarbons, functional groups and impacts")
        }),
        new Subject("O", "Physics", "5054", new Chapter[]{
            new Chapter("Motion, Forces and Energy", "speed, acceleration, forces and work"),
            new Chapter("Thermal Physics", "temperature, heat transfer and gas behaviour"),
            new Chapter("Waves and Optics", "wave properties, light and sound"),
            new Chapter("Electricity and Magnetism", "circuits, current, resistance and electromagnetism")
        }),
        new Subject("O", "Computer Science", "2210", new Chapter[]{
            new Chapter("Data Representation", "binary, hexadecimal and encoding"),
            new Chapter("Computer Systems", "hardware, networks and cybersecurity"),
            new Chapter("Algorithms and Logic", "flowcharts, pseudocode and tracing"),
            new Chapter("Programming Skills", "selection, iteration, arrays and testing")
        }),
        new Subject("O", "Business Studies", "7115", new Chapter[]{
            new Chapter("Business Activity", "enterprise, objectives and stakeholders"),
            new Chapter("People in Business", "recruitment, motivation and organisation"),
            new Chapter("Marketing", "segmentation, pricing, promotion and market research"),
            new Chapter("Operations and Finance", "production, costs, break-even and cash flow")
        }),
        new Subject("O", "Economics", "2281", new Chapter[]{
            new Chapter("Basic Economic Problem", "scarcity, choice and opportunity cost"),
            new Chapter("Markets", "demand, supply and price equilibrium"),
            new Chapter("Government and Macroeconomy", "inflation, unemployment and economic growth"),
            new Chapter("International Trade", "specialisation, exchange rates and trade")
        }),
        new Subject("O", "Accounting", "7707", new Chapter[]{
            new Chapter("Double Entry", "assets, liabilities, capital and journal entries"),
            new Chapter("Books of Original Entry", "ledgers, cash books and trial balance"),
            new Chapter("Financial Statements", "income statement and financial position"),
            new Chapter("Analysis and Control", "ratios, depreciation and corrections")
        }),
        new Subject("O", "History", "2147", new Chapter[]{
            new Chapter("Sources and Evidence", "provenance, reliability and comparison"),
            new Chapter("Causation and Consequences", "short and long-term causes"),
            new Chapter("Chronology and Change", "historical turning points and continuity"),
            new Chapter("Essay and Source Skills", "judgement, argument and evaluation")
        }),
        new Subject("O", "Geography", "2217", new Chapter[]{
            new Chapter("Earth Processes", "rocks, tectonics and weathering"),
            new Chapter("Weather and Climate", "climate data and atmospheric processes"),
            new Chapter("Population and Settlements", "urbanisation, migration and development"),
            new Chapter("Fieldwork and Maps", "map reading, enquiry and data presentation")
        }),
        new Subject("A", "English Language", "9093", new Chapter[]{
            new Chapter("Reading and Text Analysis", "language, meaning, audience and effect"),
            new Chapter("Writing and Directed Tasks", "tone, style, structure and purpose"),
            new Chapter("Language Change", "semantic, historical and contextual change"),
            new Chapter("Child Language Acquisition", "speech, development and linguistic theories")
        }),
        new Subject("A", "Mathematics", "9709", new Chapter[]{
            new Chapter("Pure Mathematics: Functions", "graphs, equations and algebraic transformations"),
            new Chapter("Calculus", "differentiation, integration and applications"),
            new Chapter("Trigonometry and Sequences", "identities, radians and series"),
            new Chapter("Mechanics and Statistics", "forces, probability and distributions")
        }),
        new Subject("A", "Further Mathematics", "9231", new Chapter[]{
            new Chapter("Further Pure Algebra", "complex numbers, roots and matrices"),
            new Chapter("Differential Equations", "first and second-order methods"),
            new Chapter("Advanced Mechanics", "momentum, forces and circular motion"),
            new Chapter("Further Probability", "continuous variables and statistical inference")
        }),
        new Subject("A", "Biology", "9700", new Chapter[]{
            new Chapter("Cells and Biological Molecules", "membranes, macromolecules and microscopy"),
            new Chapter("Enzymes and Transport", "kinetics, membrane transport and homeostasis"),
            new Chapter("Genetics and Cell Division", "DNA replication, gene expression and meiosis"),
            new Chapter("Ecology and Evolution", "selection, populations and biodiversity")
        }),
        new Subject("A", "Chemistry", "9701", new Chapter[]{
            new Chapter("Atomic Structure and Bonding", "orbitals, electronic structure and intermolecular forces"),
            new Chapter("Energetics and Equilibria", "enthalpy, equilibrium constants and Le Chatelier"),
            new Chapter("Kinetics and Electrochemistry", "rate laws, mechanisms and electrode processes"),
            new Chapter("Organic Reactions", "mechanisms, spectroscopy and synthesis")
        }),
        new Subject("A", "Physics", "9702", new Chapter[]{
            new Chapter("Physical Quantities and Mechanics", "vectors, dynamics, motion and energy"),
            new Chapter("Waves and Superposition", "diffraction, interference and stationary waves"),
            new Chapter("Electricity and Fields", "DC circuits, electric and magnetic fields"),
            new Chapter("Modern Physics", "quantum ideas, nuclear decay and uncertainty")
        }),
        new Subject("A", "Computer Science", "9618", new Chapter[]{
            new Chapter("Information Representation", "floating point, encoding and data structures"),
            new Chapter("Networks and Security", "protocols, layers, encryption and threats"),
            new Chapter("Computational Thinking", "algorithms, recursion and complexity"),
            new Chapter("Programming and Databases", "OOP, SQL, testing and development")
        }),
        new Subject("A", "Business", "9609", new Chapter[]{
            new Chapter("Business and Its Environment", "stakeholders, strategy and external forces"),
            new Chapter("Human Resources", "leadership, motivation and organisational culture"),
            new Chapter("Marketing and Operations", "mix, market analysis and quality"),
            new Chapter("Finance and Strategic Decisions", "investment appraisal, ratios and risk")
        }),
        new Subject("A", "Economics", "9708", new Chapter[]{
            new Chapter("Scarcity and Market Systems", "demand, supply, elasticity and welfare"),
            new Chapter("Market Failure", "externalities, public goods and intervention"),
            new Chapter("Macroeconomic Policy", "growth, employment, inflation and fiscal policy"),
            new Chapter("Global Economy", "trade, balance of payments and exchange rates")
        }),
        new Subject("A", "Accounting", "9706", new Chapter[]{
            new Chapter("Financial Accounting Principles", "accruals, inventories and adjustments"),
            new Chapter("Financial Statements", "partnerships, companies and reporting"),
            new Chapter("Cost and Management Accounting", "marginal costing, budgeting and variance"),
            new Chapter("Decision Making and Analysis", "investment, ratios and interpretations")
        }),
        new Subject("A", "History", "9489", new Chapter[]{
            new Chapter("Historical Interpretation", "causation, change and historical debate"),
            new Chapter("Primary Source Evaluation", "purpose, context, provenance and reliability"),
            new Chapter("Comparative Thematic Study", "similarities, differences and periodisation"),
            new Chapter("Essay Structure", "balanced arguments, evidence and evaluation")
        }),
        new Subject("A", "Psychology", "9990", new Chapter[]{
            new Chapter("Research Methods", "experiments, sampling, ethics and reliability"),
            new Chapter("Core Psychological Approaches", "cognitive, biological and learning theories"),
            new Chapter("Data and Analysis", "design, interpretation and conclusions"),
            new Chapter("Application and Evaluation", "studies, debate and psychological practice")
        })
    };
    public static int subjectCount(String level) {
        int count=0;for(Subject s:SUBJECTS)if(s.level.equals(level))count++;return count;
    }
    public static Subject at(String level,int position){
        int n=0;for(Subject s:SUBJECTS)if(s.level.equals(level)){
            if(n==position)return s;n++;
        }
        return null;
    }
}
