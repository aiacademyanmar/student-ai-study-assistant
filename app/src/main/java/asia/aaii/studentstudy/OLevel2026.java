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
        new Subject("English Language","1123","2024–2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-english-language-1123/",true,new String[]{
            "Reading :: understanding explicit and implied meaning :: summary of relevant information :: analysing facts and opinions :: writer's language and effect :: selection of textual evidence",
            "Writing :: audience, purpose and form :: directed writing and argument :: narrative and descriptive composition :: organisation, register and style :: grammar, spelling and punctuation"
        }),
        new Subject("Mathematics (Syllabus D)","4024","2025–2027","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-mathematics-d-4024/",true,new String[]{
            "Number :: number types, sets and operations :: fractions and decimals :: powers, roots and indices :: standard form, estimation and bounds :: ratio, percentages and rates",
            "Algebra and graphs :: algebraic expressions and fractions :: equations and inequalities :: sequences :: direct and inverse proportion :: graphs and functions",
            "Coordinate geometry :: coordinates and gradients :: straight-line equations :: length and midpoint :: parallel and perpendicular lines",
            "Geometry :: angles, constructions and bearings :: similarity and symmetry :: geometrical properties :: circle theorems",
            "Mensuration :: units and area :: perimeter and circles :: surface area and volume :: composite shapes",
            "Trigonometry :: Pythagoras' theorem :: right-angle trigonometry :: sine and cosine rules :: geometric applications",
            "Transformations and vectors :: reflection rotation translation enlargement :: vector magnitude and direction :: vector geometry",
            "Probability :: probability scales and relative frequency :: sample spaces and combined events :: tree diagrams and Venn diagrams",
            "Statistics :: data classification and interpretation :: averages and spread :: frequency charts and diagrams :: cumulative frequency and histograms"
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
        new Subject("Chemistry","5070","2026–2028","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-chemistry-5070/",true,new String[]{
            "States of matter :: properties and particle arrangements :: change of state :: diffusion",
            "Atoms, elements and compounds :: elements mixtures and compounds :: atomic structure and isotopes :: ionic covalent metallic bonding",
            "Stoichiometry :: chemical formulae and equations :: relative masses and moles :: concentration and quantitative calculations",
            "Electrochemistry :: electrolysis of molten and aqueous compounds :: electrode reactions :: industrial applications",
            "Chemical energetics :: exothermic and endothermic :: energy profile diagrams :: bond energy calculations",
            "Chemical reactions :: physical and chemical changes :: rates of reaction :: reversible reactions and equilibrium :: oxidation and reduction",
            "Acids, bases and salts :: acid and base properties :: pH and neutralisation :: salts and preparation techniques",
            "The Periodic Table :: periodic trends :: Group I alkali metals :: Group VII halogens :: transition elements",
            "Metals :: metal properties and alloys :: reactivity series :: extraction and corrosion",
            "Chemistry of the environment :: water and treatment :: air quality and pollutants :: greenhouse gases and environmental protection",
            "Organic chemistry :: hydrocarbon families :: addition and substitution reactions :: alcohols, organic acids and polymers",
            "Experimental techniques and chemical analysis :: separation and purification methods :: chemical tests :: qualitative analysis, safety and interpretation"
        }),
        new Subject("Physics","5054","2026–2028","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-physics-5054/",true,new String[]{
            "Motion, forces and energy :: physical quantities and measurement :: motion and speed-time graphs :: mass, force and moments :: momentum and collisions :: energy work and power :: pressure",
            "Thermal physics :: kinetic particle model :: thermal expansion and temperature :: specific heat capacity :: melting boiling and evaporation :: heat transfer",
            "Waves :: wave properties and speed :: light reflection and refraction :: electromagnetic spectrum :: sound",
            "Electricity and magnetism :: magnetism and fields :: current and potential difference :: electrical circuits and resistance :: electric safety and power :: electromagnetic effects",
            "Nuclear physics :: atomic structure :: radioactivity and nuclear emissions :: half-life and nuclear safety",
            "Space physics :: Earth and Solar System :: stars and their life cycles :: galaxies and cosmology"
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
            "The basic economic problem :: scarcity, wants and choice :: factors of production :: opportunity cost :: production possibility curves",
            "The allocation of resources :: market demand and supply :: market equilibrium :: price mechanism :: price elasticity",
            "Microeconomic decision makers :: money and banking :: households and workers :: trade unions :: firms and business scale",
            "Government and the macroeconomy :: government objectives and indicators :: inflation and unemployment :: economic growth :: fiscal and monetary policies",
            "Economic development :: living standards and development :: population change :: poverty and income inequality",
            "International trade and globalisation :: specialisation and international trade :: free trade and protectionism :: foreign exchange rates :: balance of payments and multinational companies"
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
        new Subject("History","2147","2024–2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-history-2147/",true,new String[]{
            "Core Option A — The nineteenth century: nation states, 1848–1914 :: revolutions of 1848 :: Italian unification :: German unification :: US Civil War :: overseas imperial expansion :: causes of First World War",
            "Core Option B — The twentieth century: international relations since 1919 :: Treaty of Versailles :: League of Nations :: Hitler's foreign policy :: origins of Cold War :: United States containment of communism :: Soviet control of Eastern Europe",
            "Depth Study A — The First World War, 1914–18 :: causes and major phases :: experiences of combatants :: war's effects",
            "Depth Study B — Germany, 1918–45 :: Weimar Germany :: rise of Nazis :: Nazi control and society",
            "Depth Study C — Russia, 1905–41 :: revolutions in Russia :: Lenin's policies :: Stalin and the Soviet Union",
            "Depth Study D — The United States, 1919–41 :: economic boom :: Great Depression :: New Deal",
            "Depth Study E — The Second World War in Europe and the Asia–Pacific, 1939–c.1945 :: war in Europe :: war in the Asia-Pacific :: causes and consequences"
        }),
        new Subject("Geography","2217","2026","https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-o-level-geography-2217/",true,new String[]{
            "Theme 1: Population and settlement :: population dynamics and migration :: population structure :: population density and distribution :: settlement and service provision :: urbanisation and urban land use",
            "Theme 2: The natural environment :: earthquakes and volcanoes :: rivers and coastal processes :: weather and climate :: natural vegetation and tropical environments",
            "Theme 3: Economic development :: economic sectors and industry :: food production and agriculture :: tourism :: energy and water resources :: environmental risks and sustainability"
        })
    };
    public static int topicCount(){int k=0;for(Subject s:SUBJECTS)k+=s.topics.length;return k;}
    public static int subtopicCount(){int k=0;for(Subject s:SUBJECTS)for(Topic t:s.topics)k+=t.subtopics.length;return k;}
    public static int lessonCount(){return subtopicCount()*LESSON_STAGES.length;}
}
