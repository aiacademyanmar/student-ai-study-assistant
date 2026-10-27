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
        new Subject("Additional Mathematics","4037","2025–2027","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-mathematics-additional-4037/",true,new String[]{
            "Functions :: domain and range :: composite and inverse functions :: graphs",
            "Quadratic functions :: completing the square :: discriminant :: maximum and minimum",
            "Factors of polynomials :: remainder theorem :: factor theorem :: polynomial factorisation",
            "Equations, inequalities and graphs :: linear and quadratic inequalities :: graph intersections :: solution regions",
            "Simultaneous equations :: linear and nonlinear systems :: algebraic substitution :: solutions",
            "Logarithmic and exponential functions :: log laws :: exponential equations :: graph interpretation",
            "Straight-line graphs :: gradient :: line equations :: linear relationships",
            "Coordinate geometry of the circle :: circle equations :: centre and radius :: tangents",
            "Circular measure :: radian measure :: arc length :: sector area",
            "Trigonometry :: trigonometric identities :: equations :: graphs",
            "Permutations and combinations :: arrangements :: selections :: counting strategies",
            "Series :: arithmetic progression :: geometric progression :: binomial expansion",
            "Vectors in two dimensions :: position vectors :: magnitude and direction :: geometry applications",
            "Calculus :: differentiation :: integration :: applications"
        }),
        new Subject("Biology","5090","2026–2028","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-biology-5090/",true,new String[]{
            "Cells :: cell structures :: microscopy and magnification :: specialised cells",
            "Classification :: classification groups :: identification keys :: binomial naming",
            "Movement into and out of cells :: diffusion :: osmosis :: active transport",
            "Biological molecules :: carbohydrates proteins and lipids :: chemical food tests :: DNA",
            "Enzymes :: enzyme action :: temperature and pH :: investigation",
            "Plant nutrition :: photosynthesis equation :: leaf adaptation :: limiting factors",
            "Transport in flowering plants :: xylem and phloem :: transpiration :: translocation",
            "Human nutrition :: diet and digestion :: digestive system :: absorption",
            "Human gas exchange :: air passage :: alveoli :: ventilation",
            "Respiration :: aerobic respiration :: anaerobic respiration :: energy transfer",
            "Transport in humans :: heart and blood vessels :: blood components :: circulation",
            "Disease and immunity :: transmission :: pathogens :: immunity and prevention",
            "Excretion :: kidneys :: urea :: water balance",
            "Coordination and control :: nervous system :: reflexes :: hormonal control",
            "Coordination and response in plants :: plant growth responses :: tropisms :: auxins",
            "Development of organisms and continuity of life :: reproduction :: sexual and asexual reproduction :: life cycles",
            "Inheritance :: chromosomes and genes :: genetic crosses :: variation",
            "Biotechnology and genetic modification :: fermentation :: microorganisms :: genetic engineering",
            "Relationships of organisms with one another and with the environment :: food chains and webs :: population interactions :: conservation"
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
            "Data representation :: binary and hexadecimal :: text images sound :: compression",
            "Data transmission :: packets :: transmission protocols :: error detection",
            "Hardware :: CPU :: input output and storage :: sensors",
            "Software :: system and application software :: operating systems :: interrupts",
            "The internet and its uses :: internet and www :: network security :: online services",
            "Automated and emerging technologies :: automated systems :: robots :: emerging AI",
            "Algorithm design and problem-solving :: decomposition :: pseudocode flowcharts :: testing and dry runs",
            "Programming :: variables and data types :: selection and iteration :: arrays and subroutines",
            "Databases :: table fields and records :: query logic :: SQL",
            "Boolean logic :: logic gates :: truth tables :: Boolean expressions"
        }),
        new Subject("Business Studies","7115","2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-business-studies-7115/",true,new String[]{
            "Understanding business activity :: business purpose and classification :: enterprise and ownership :: stakeholders",
            "People in business :: motivation :: recruitment and training :: communication",
            "Marketing :: market research :: marketing mix :: segmenting markets",
            "Operations management :: production methods :: break-even and costs :: quality control",
            "Financial information and decisions :: finance sources :: cash-flow forecasts :: statements and ratios",
            "External influences on business activity :: government policy :: ethical and environmental issues :: globalisation"
        }),
        new Subject("Economics","2281","2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-economics-2281/",true,new String[]{
            "The Basic Economic Problem :: scarcity and choice :: factors of production :: opportunity cost and PPC",
            "The Allocation of Resources :: demand and supply :: market equilibrium :: price elasticity",
            "Microeconomic Decision Makers :: money and banking :: households and workers :: firms and trade unions",
            "Government and the Macroeconomy :: economic growth :: inflation and unemployment :: fiscal and monetary policies",
            "Economic Development :: living standards :: population and development :: poverty and income distribution",
            "International Trade and Globalisation :: specialisation and trade :: exchange rates :: balance of payments and multinationals"
        }),
        new Subject("Accounting","7707","2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-accounting-7707/",true,new String[]{
            "The fundamentals of accounting :: bookkeeping and accounting :: accounting equation :: assets and liabilities",
            "Sources and recording of data :: business documents :: double-entry and ledgers :: books of prime entry",
            "Verification of accounting records :: trial balance :: bank reconciliation :: control accounts and errors",
            "Accounting procedures :: depreciation :: accruals and prepayments :: irrecoverable debts",
            "Preparation of financial statements :: sole traders and partnerships :: limited companies and clubs :: manufacturing accounts and incomplete records",
            "Analysis and interpretation :: profitability ratios :: liquidity :: limitations and users",
            "Accounting principles and policies :: accounting principles :: international standards :: choosing accounting policies"
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
