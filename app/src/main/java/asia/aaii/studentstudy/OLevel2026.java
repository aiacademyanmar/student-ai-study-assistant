package asia.aaii.studentstudy;
import java.util.ArrayList;
import java.util.List;
/** Independent original syllabus study map, not a replacement for Cambridge's official PDF.
 * Exact top-level content names are distinguished from pedagogic approximations.
 * 2026 exam-year track; always cross-check the official syllabus for zone/options/updates.
 */
public final class OLevel2026 {
    private OLevel2026(){}
    public static final class Subject {
        public final String name,code,years,officialUrl;
        public final boolean officialTopicHeadings;
        public final Topic[] topics;
        Subject(String n,String c,String y,String url,boolean official,String[] spec){
            name=n;code=c;years=y;officialUrl=url;officialTopicHeadings=official;
            topics=new Topic[spec.length];
            for(int i=0;i<spec.length;i++){
                String[] parts=spec[i].split(" :: ");
                String[] subs=new String[parts.length-1];
                System.arraycopy(parts,1,subs,0,subs.length);
                topics[i]=new Topic(parts[0],subs);
            }
        }
    }
    public static final class Topic{
        public final String name; public final String[] subtopics;
        Topic(String n,String[] topics){name=n;subtopics=topics;}
    }
    public static final String[] LESSON_STAGES={"Concept & Explanation","Worked Examples & Applications","Exam Skills & Review"};
    public static final String[] EXERCISE_STAGES={"Concept Check","MCQ","Structured Answer","Error Spotting","Timed Practice"};
    public static final Subject[] SUBJECTS={
        new Subject("English Language","1123","2024–2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-english-language-1123/",false,new String[]{
            "Reading Comprehension :: explicit information :: inference and interpretation :: writer's methods and effects :: evidence and quotation",
            "Summary Skills :: identify relevant details :: concise paraphrasing :: organise information :: word-limit accuracy",
            "Language Analysis :: tone and attitude :: imagery and vocabulary :: sentence effect :: audience and purpose",
            "Directed Writing :: discursive arguments :: persuasive writing :: formal letters and reports :: speeches and articles",
            "Descriptive Composition :: sensory detail :: imagery and precision :: structure and transitions",
            "Narrative Composition :: plot and pace :: character and voice :: opening and ending",
            "Argumentative Composition :: claim and reasons :: counter-arguments :: evidence and conclusion",
            "Vocabulary and Grammar :: sentence variety :: precise diction :: punctuation and proof-reading",
            "Exam Reading Practice :: unseen passages :: response structure :: time management",
            "Exam Writing Practice :: planning and drafting :: editing and assessment criteria :: checking register"
        }),
        new Subject("Mathematics (Syllabus D)","4024","2025–2027","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-mathematics-d-4024/",false,new String[]{
            "Number :: integers and fractions :: decimals and percentages :: ratio and proportion :: standard form and bounds",
            "Set Language and Notation :: set symbols :: Venn diagrams :: set operations",
            "Algebraic Manipulation :: expanding and factorising :: algebraic fractions :: formula rearrangement",
            "Equations and Inequalities :: linear equations :: simultaneous equations :: quadratic equations :: inequalities",
            "Sequences :: arithmetic sequences :: nth term :: patterns",
            "Functions and Graphs :: function notation :: plotting and interpreting graphs :: quadratic and cubic graphs",
            "Coordinate Geometry :: gradient and intercept :: midpoint and distance :: lines and parallelism",
            "Geometry :: angle rules and polygons :: congruence and similarity :: circle properties",
            "Mensuration :: perimeter and area :: surface area :: volume of solids",
            "Trigonometry :: right-angle trig :: sine and cosine rules :: bearings and elevation",
            "Vectors :: vector notation :: column vectors :: geometric problems",
            "Transformations :: reflection and rotation :: translation :: enlargement",
            "Probability :: sample spaces :: combined events :: tree diagrams",
            "Statistics :: data handling :: averages and spread :: histograms and cumulative frequency",
            "Practical Problem Solving :: rates and conversions :: percent change :: estimation and checking"
        }),
        new Subject("Additional Mathematics","4037","2025–2027","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-mathematics-additional-4037/",false,new String[]{
            "Functions :: domain and range :: composite functions :: inverse functions",
            "Quadratic Functions :: quadratic expressions :: discriminant :: graph transformations",
            "Factors of Polynomials :: factor and remainder theorems :: polynomial division :: roots and identities",
            "Equations and Inequalities :: simultaneous and polynomial equations :: quadratic inequalities :: graphs and intersections",
            "Indices and Surds :: laws of indices :: simplifying surds :: rationalising denominators",
            "Logarithmic and Exponential Functions :: laws of logarithms :: exponential equations :: graph interpretation",
            "Straight-Line Graphs :: linear form :: gradient and intercept :: curve linearisation",
            "Coordinate Geometry :: equations of circles :: tangents and normals :: distance problems",
            "Circular Measure :: radians :: arc length :: sector area",
            "Trigonometry :: identities and equations :: trigonometric graphs :: compound angle formulae",
            "Permutations and Combinations :: counting principles :: permutations :: combinations",
            "Series and Binomial :: arithmetic and geometric progressions :: binomial expansions :: coefficients",
            "Differentiation :: rules of differentiation :: tangents and normals :: stationary points",
            "Integration :: indefinite integrals :: definite integrals :: area under curves",
            "Vectors in Two Dimensions :: vector magnitude :: position vectors :: geometric applications"
        }),
        new Subject("Biology","5090","2026–2028","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-biology-5090/",false,new String[]{
            "Characteristics and Classification :: characteristics of living organisms :: classification systems :: binomial naming",
            "Organisation of the Organism :: cell structure :: specialised cells :: tissues and organ systems",
            "Movement Into and Out of Cells :: diffusion :: osmosis :: active transport",
            "Biological Molecules :: carbohydrates and proteins :: lipids and water :: food tests",
            "Enzymes :: enzyme action :: temperature and pH effects :: investigating enzyme activity",
            "Plant Nutrition :: photosynthesis :: leaf structure :: limiting factors",
            "Human Nutrition :: diet and nutrients :: digestive system :: digestion and absorption",
            "Transport in Plants :: xylem and phloem :: transpiration :: translocation",
            "Transport in Animals :: circulation and the heart :: blood vessels :: blood components",
            "Diseases and Immunity :: pathogens :: transmission and prevention :: immune response and vaccination",
            "Gas Exchange in Humans :: breathing system :: alveoli :: gas exchange and exercise",
            "Respiration :: aerobic respiration :: anaerobic respiration :: energy release",
            "Excretion in Humans :: kidney structure :: filtration and reabsorption :: homeostasis",
            "Coordination and Response :: nervous system :: reflexes :: hormones and tropisms",
            "Drugs :: effects of drugs :: tobacco and alcohol :: dependence and risk",
            "Reproduction :: asexual and sexual :: flower reproduction :: human reproduction",
            "Inheritance :: DNA genes chromosomes :: monohybrid crosses :: genetic variation",
            "Variation and Selection :: continuous and discontinuous variation :: natural selection :: adaptation",
            "Organisms and Environment :: food chains and webs :: energy flow :: nutrient cycles",
            "Biotechnology and Genetic Modification :: microorganisms and industry :: genetic engineering :: ethical considerations",
            "Human Influences on Ecosystems :: habitat destruction :: pollution and conservation :: sustainability",
            "Practical and Experimental Skills :: variables and fair tests :: graphs and observations :: data analysis"
        }),
        new Subject("Chemistry","5070","2026–2028","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-chemistry-5070/",false,new String[]{
            "States of Matter :: particle model :: state changes :: diffusion",
            "Atoms, Elements and Compounds :: atomic structure :: periodic arrangement :: ions and isotopes",
            "Chemical Bonding :: ionic bonding :: covalent bonding :: metallic bonding",
            "Stoichiometry :: chemical formulae :: moles and equations :: concentration and gas volumes",
            "Electrochemistry :: electrolysis :: electrode processes :: industrial applications",
            "Chemical Energetics :: exothermic and endothermic :: energy profiles :: bond energies",
            "Chemical Reactions :: reaction rates :: reversible reactions :: redox processes",
            "Acids, Bases and Salts :: pH and indicators :: neutralisation :: salt preparation",
            "Periodic Table :: groups and trends :: Group I :: Group VII and transition elements",
            "Metals :: reactivity series :: extraction and alloys :: corrosion and prevention",
            "Chemistry of the Environment :: air and water :: climate and pollutants :: fertilisers and ecosystems",
            "Organic Chemistry :: hydrocarbons :: alcohols and carboxylic acids :: polymers",
            "Experimental Techniques :: purification and separation :: qualitative analysis :: planning and safety"
        }),
        new Subject("Physics","5054","2026–2028","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-physics-5054/",false,new String[]{
            "Physical Quantities and Measurement :: SI units :: measurement instruments :: uncertainty and significant figures",
            "Motion :: speed and velocity :: acceleration :: motion graphs",
            "Forces :: force and mass :: Newton's laws :: turning effects",
            "Momentum :: momentum conservation :: impulse :: collisions",
            "Energy, Work and Power :: energy stores and transfer :: efficiency :: power",
            "Pressure :: pressure in solids :: pressure in fluids :: hydraulic applications",
            "Thermal Properties :: kinetic particle model :: thermal expansion :: changes of state",
            "Thermal Processes :: conduction :: convection :: radiation",
            "Wave Properties :: amplitude frequency wavelength :: wave speed :: reflection and refraction",
            "Light :: lenses and images :: reflection and refraction :: dispersion",
            "Electromagnetic Spectrum :: spectrum regions :: uses and hazards :: radiation",
            "Sound :: production and transmission :: speed and echoes :: frequency and pitch",
            "Magnetism :: magnetic fields :: electromagnets :: motors",
            "Electrical Quantities :: current voltage resistance :: Ohm's law :: electrical power",
            "Electric Circuits :: series circuits :: parallel circuits :: safety and energy",
            "Electromagnetic Effects :: induction :: transformers :: motors and generators",
            "Nuclear Physics :: atomic model :: radioactivity :: half-life",
            "Space Physics :: Earth and Solar System :: planetary orbits :: stars and universe",
            "Practical Skills :: planning experiments :: graphs and measurement :: evaluation of evidence"
        }),
        new Subject("Computer Science","2210","2026–2028","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-computer-science-2210/",true,new String[]{
            "Data Representation :: number systems :: binary arithmetic :: character encoding :: images and sound",
            "Data Transmission :: packets and switching :: transmission methods :: error detection",
            "Hardware :: CPU and memory :: input and output :: storage and sensors",
            "Software :: system software :: applications :: interrupts",
            "The Internet and Its Uses :: internet and web :: network hardware :: security threats",
            "Automated and Emerging Technologies :: automated systems :: robotics :: artificial intelligence",
            "Algorithm Design and Problem-Solving :: decomposition :: pseudocode and flowcharts :: trace tables and testing",
            "Programming :: variables and data types :: selection and iteration :: arrays and procedures",
            "Databases :: database structure :: SQL basics :: validation and integrity",
            "Boolean Logic :: logic gates :: truth tables :: logic circuits"
        }),
        new Subject("Business Studies","7115","2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-business-studies-7115/",true,new String[]{
            "Understanding Business Activity :: business purpose and classification :: enterprise and growth :: ownership and objectives",
            "People in Business :: motivation and management :: recruitment and training :: internal and external communication",
            "Marketing :: market segmentation :: market research :: product price place promotion",
            "Operations Management :: production methods :: costs and break-even :: quality and location",
            "Financial Information and Decisions :: sources of finance :: cash flow and working capital :: financial statements and ratios",
            "External Influences on Business Activity :: government policies :: ethical and environmental issues :: globalisation and exchange rates"
        }),
        new Subject("Economics","2281","2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-economics-2281/",true,new String[]{
            "The Basic Economic Problem :: scarcity and choice :: factors of production :: opportunity cost and PPC",
            "The Allocation of Resources :: demand and supply :: market equilibrium :: price elasticity",
            "Microeconomic Decision Makers :: money and banking :: households and workers :: firms and trade unions",
            "Government and the Macroeconomy :: economic growth :: inflation and unemployment :: fiscal and monetary policies",
            "Economic Development :: living standards :: population and development :: poverty and income distribution",
            "International Trade and Globalisation :: specialisation and trade :: exchange rates :: balance of payments and multinationals"
        }),
        new Subject("Accounting","7707","2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-accounting-7707/",false,new String[]{
            "The Fundamentals of Accounting :: role and purpose :: accounting concepts :: assets liabilities capital",
            "Sources and Recording of Data :: business documents :: double-entry bookkeeping :: ledgers and trial balance",
            "Verification of Accounting Records :: bank reconciliation :: control accounts :: errors and suspense",
            "Accounting Procedures :: depreciation and disposal :: bad debts and provision :: accruals and prepayments",
            "Financial Statements of Sole Traders :: income statements :: statement of financial position :: end-of-year adjustments",
            "Partnership Accounts :: appropriation accounts :: capital and current accounts :: changes in partnerships",
            "Club and Non-Profit Accounts :: receipts and payments :: income and expenditure :: accumulated fund",
            "Manufacturing Accounts :: cost classification :: cost of production :: work-in-progress",
            "Limited Company Accounts :: share capital :: profit distribution :: financial reporting",
            "Incomplete Records :: statement of affairs :: control techniques :: reconstructing profit",
            "Analysis and Interpretation :: profitability :: liquidity :: decision-making with ratios"
        }),
        new Subject("History","2147","2024–2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-history-2147/",false,new String[]{
            "Core Content: International Relations :: peace settlements after war :: League of Nations :: international diplomacy",
            "Core Content: Conflict and Peace :: causes of conflict :: appeasement :: Cold War origins",
            "Historical Depth Study Options :: selected country case study :: society and economy :: political change",
            "Source-Based Enquiry :: origin purpose and context :: reliability and usefulness :: corroboration",
            "Causation and Consequence :: long and short-term causes :: short and long-term effects :: ranked judgement",
            "Change and Continuity :: turning points :: comparing periods :: historical significance",
            "Historical Interpretation :: different perspectives :: bias and provenance :: evaluating claims",
            "Structured Historical Essays :: argument structure :: precise evidence :: balanced conclusion",
            "Exam Source and Essay Skills :: command words :: time allocation :: criteria-aware responses"
        }),
        new Subject("Geography","2217","2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-geography-2217/",false,new String[]{
            "Population Dynamics :: population change :: age and sex structures :: migration",
            "Settlements and Urbanisation :: settlement patterns :: urban issues :: management strategies",
            "Rocks, Relief and Plate Processes :: tectonic activity :: weathering and erosion :: landforms",
            "Weather and Climate :: weather instruments :: rainfall patterns :: climate systems",
            "Rivers and Coasts :: river processes :: coastal processes :: hazards and management",
            "Ecosystems and Natural Environments :: biomes :: rainforests :: environmental threats",
            "Economic Development :: industry and services :: measuring development :: inequality",
            "Food, Water and Energy Resources :: food security :: water supplies :: energy choices",
            "Environmental Risks and Sustainability :: pollution :: climate change :: resource management",
            "Geographical Skills and Fieldwork :: map work :: graphs and diagrams :: enquiry data and evaluation"
        })
    };
    public static int topicCount(){int k=0;for(Subject s:SUBJECTS)k+=s.topics.length;return k;}
    public static int subtopicCount(){int k=0;for(Subject s:SUBJECTS)for(Topic t:s.topics)k+=t.subtopics.length;return k;}
    public static int lessonCount(){return subtopicCount()*LESSON_STAGES.length;}
}
