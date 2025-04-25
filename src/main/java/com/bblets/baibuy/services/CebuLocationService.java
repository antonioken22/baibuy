package com.bblets.baibuy.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * Provides read-only access to Cebu location data (Municipalities/Cities and
 * Barangays).
 * The data is statically initialized and immutable.
 */
@Service
public class CebuLocationService {

        // The core data structure: Map<MunicipalityName, List<BarangayName>>
        private static final Map<String, List<String>> cebuLocations;

        // Static initializer block to populate the map when the class is loaded.
        static {
                Map<String, List<String>> mutableMap = new LinkedHashMap<>();

                // --- ALCANTARA ---
                mutableMap.put("ALCANTARA", List.of(
                                "CABADIANGAN", "CABIL-ISAN", "CANDABONG", "LAWAAN", "MANGA",
                                "PALANAS", "POBLACION", "POLO", "SALAGMAYA"));

                // --- ALCOY ---
                mutableMap.put("ALCOY", List.of(
                                "ATABAY", "DAAN-LUNGSOD", "GUIWANG", "NUG-AS", "PASOL",
                                "POBLACION", "PUGALO", "SAN AGUSTIN"));

                // --- ALEGRIA ---
                mutableMap.put("ALEGRIA", List.of(
                                "COMPOSTELA", "GUADALUPE", "LEGASPI", "LEPANTO", "MADRIDEJOS",
                                "MONTPELLER", "POBLACION", "SANTA FILOMENA", "VALENCIA"));

                // --- ALOGUINSAN ---
                mutableMap.put("ALOGUINSAN", List.of(
                                "ANGILAN", "BOJO", "BONBON", "ESPERANZA", "KANDINGAN",
                                "KANTABOGON", "KAWASAN", "OLANGO", "POBLACION", "PUNAY",
                                "ROSARIO", "SAKSAK", "TAMPA-AN", "TOYOKON", "ZARAGOSA"));

                // --- ARGAO ---
                mutableMap.put("ARGAO", List.of(
                                "ALAMBIJUD", "ANAJAO", "APO", "BALAAS", "BALISONG", "BINLOD",
                                "BOGO", "BUG-OT", "BULASA", "BUTONG", "CALAGASAN", "CANBANTUG",
                                "CANBANUA", "CANSUJE", "CAPIO-AN", "CASAY", "CATANG", "COLAWIN",
                                "CONALUM", "GUIWANON", "GUTLANG", "JAMPANG", "JOMGAO", "LAMACAN",
                                "LANGTAD", "LANGUB", "LAPAY", "LENGIGON", "LINUT-OD", "MABASA",
                                "MANDILIKIT", "MOMPELLER", "PANADTARAN", "POBLACION", "SUA",
                                "SUMAGUAN", "TABAYAG", "TALAGA", "TALAYTAY", "TALO-OT", "TIGUIB",
                                "TULANG", "TULIC", "UBAUB", "USMAD"));

                // --- ASTURIAS ---
                mutableMap.put("ASTURIAS", List.of(
                                "AGBANGA", "AGTUGOP", "BAGO", "BAIRAN", "BANBAN", "BAYE", "BOG-O",
                                "KALUANGAN", "LANAO", "LANGUB", "LOOC NORTE", "LUNAS", "MAGCALAPE",
                                "MANGUIAO", "NEW BAGO", "OWAK", "POBLACION", "SAKSAK", "SAN ISIDRO",
                                "SAN ROQUE", "SANTA LUCIA", "SANTA RITA", "TAG-AMAKAN", "TAGBUBONGA",
                                "TUBIGAGMANOK", "TUBOD", "UBOGON"));

                // --- BADIAN ---
                mutableMap.put("BADIAN", List.of(
                                "ALAWIJAO", "BALHAAN", "BANHIGAN", "BASAK", "BASIAO", "BATO",
                                "BUGAS", "CALANGCANG", "CANDIIS", "DAGATAN", "DOBDOB", "GINABLAN",
                                "LAMBUG", "MALABAGO", "MALHIAO", "MANDUYONG", "MATUTINAO", "PATONG",
                                "POBLACION", "SANLAGAN", "SANTICON", "SOHOTON", "SULSUGAN",
                                "TALAYONG", "TAYTAY", "TIGBAO", "TIGUIB", "TUBOD", "ZARAGOSA"));

                // --- BALAMBAN ---
                mutableMap.put("BALAMBAN", List.of(
                                "ABUCAYAN", "ALIWANAY", "ARPILI", "BALIWAGAN (POB.)", "BAYONG",
                                "BIASONG", "BUANOY", "CABAGDALAN", "CABASIANGAN", "CAMBUHAWE",
                                "CANSOMOROY", "CANTIBAS", "CANTUOD", "DUANGAN", "GAAS", "GINATILAN",
                                "HINGATMONAN", "LAMESA", "LIKI", "LUCA", "MATUN-OG", "NANGKA",
                                "PONDOL", "PRENZA", "SANTA CRUZ-SANTO NIÑO (POB.)", "SINGSING",
                                "SUNOG", "VITO"));

                // --- BANTAYAN ---
                mutableMap.put("BANTAYAN", List.of(
                                "ATOP-ATOP", "BAIGAD", "BANTIGUE (POB.)", "BAOD", "BINAOBAO (POB.)",
                                "BOTIGUES", "DOONG", "GUIWANON", "HILOTONGAN", "KABAC", "KABANGBANG",
                                "KAMPINGGANON", "KANGKAIBE", "LIPAYRAN", "LUYONGBAYBAY", "MOJON",
                                "OBO-OB", "PATAO", "PUTIAN", "SILLON", "SUBA (POB.)", "SULANGAN",
                                "SUNGKO", "TAMIAO", "TICAD (POB.)"));

                // --- BARILI ---
                mutableMap.put("BARILI", List.of(
                                "AZUCENA", "BAGAKAY", "BALAO", "BOLOCBOLOC", "BUDBUD",
                                "BUGTONG KAWAYAN", "CABCABAN", "CAGAY", "CAMPANGGA", "CANDUGAY",
                                "DAKIT", "GILOCTOG", "GIWANON", "GUIBUANGAN", "GUNTING", "HILASGASAN",
                                "JAPITAN", "KALUBIHAN", "KANGDAMPAS", "LUHOD", "LUPO", "LUYO",
                                "MAGHANOY", "MAIGANG", "MALOLOS", "MANTALONGON", "MANTAYUPAN",
                                "MAYANA", "MINOLOS", "NABUNTURAN", "NASIPIT", "PANCIL", "PANGPANG",
                                "PARIL", "PATUPAT", "POBLACION", "SAN RAFAEL", "SANTA ANA", "SAYAW",
                                "TAL-OT", "TUBOD", "VITO"));

                // --- BOGO CITY ---
                mutableMap.put("BOGO CITY", List.of(
                                "ANONANG NORTE", "ANONANG SUR", "BANBAN", "BINABAG", "BUNGTOD (POB.)",
                                "CARBON (POB.)", "CAYANG", "COGON (POB.)", "DAKIT", "DON PEDRO RODRIGUEZ",
                                "GAIRAN", "GUADALUPE", "LA PAZ", "LA PURISIMA CONCEPCION (POB.)",
                                "LIBERTAD", "LOURDES (POB.)", "MALINGIN", "MARANGOG", "NAILON", "ODLOT",
                                "PANDAN (PANDAN HEIGHTS)", "POLAMBATO", "SAMBAG (POB.)",
                                "SAN VICENTE (POB.)", "SANTO NIÑO", "SANTO ROSARIO (POB.)", "SIOCON",
                                "SUDLONON", "TAYTAYAN"));

                // --- BOLJOON ---
                mutableMap.put("BOLJOON", List.of(
                                "ARBOR", "BACLAYAN", "EL PARDO", "GRANADA", "LOWER BECERRIL", "LUNOP",
                                "NANGKA", "POBLACION", "SAN ANTONIO", "SOUTH GRANADA", "UPPER BECERRIL"));

                // --- BORBON ---
                mutableMap.put("BORBON", List.of(
                                "BAGACAY", "BILI", "BINGAY", "BONGDO", "BONGDO GUA", "BONGOYAN",
                                "CADARUHAN", "CAJEL", "CAMPUSONG", "CLAVERA",
                                "DON GREGORIO ANTIGUA (TAYTAYAN)", "LAAW", "LUGO", "MANAGASE",
                                "POBLACION", "SAGAY", "SAN JOSE", "TABUNAN", "TAGNUCAN"));

                // --- CARCAR CITY ---
                mutableMap.put("CARCAR CITY", List.of(
                                "BOLINAWAN", "BUENAVISTA", "CALIDNGAN", "CAN-ASUJAN", "GUADALUPE",
                                "LIBURON", "NAPO", "OCANA", "PERRELOS", "POBLACION I", "POBLACION II",
                                "POBLACION III", "TUYOM", "VALENCIA", "VALLADOLID"));

                // --- CARMEN ---
                mutableMap.put("CARMEN", List.of(
                                "BARING", "CANTIPAY", "CANTUKONG", "CANTUMOG", "CAURASAN",
                                "COGON EAST", "COGON WEST", "CORTE", "DAWIS NORTE", "DAWIS SUR",
                                "HAGNAYA", "IPIL", "LANIPGA", "LIBORON", "LOWER NATIMAO-AN", "LUYANG",
                                "POBLACION", "PUENTE", "SAC-ON", "TRIUMFO", "UPPER NATIMAO-AN"));

                // --- CATMON ---
                mutableMap.put("CATMON", List.of(
                                "AGSUWAO", "AMANCION", "ANAPOG", "BACTAS", "BASAK", "BINONGKALAN",
                                "BONGYAS", "CABUNGAAN", "CAMBANGKAYA", "CAN-IBUANG", "CATMONDAAN",
                                "CORAZON (POB.)", "DUYAN", "FLORES (POB.)", "GINABUCAN", "MACAAS",
                                "PANALIPAN", "SAN JOSE POB. (CATADMAN)", "TABILI", "TINABYONAN"));

                // --- CEBU CITY ---
                mutableMap.put("CEBU CITY", List.of(
                                "ADLAON", "AGSUNGOT", "APAS", "BABAG", "BACAYAN", "BANILAD",
                                "BASAK PARDO", "BASAK SAN NICOLAS", "BINALIW", "BONBON",
                                "BUDLA-AN (POB.)", "BUHISAN", "BULACAO", "BUOT-TAUP PARDO",
                                "BUSAY (POB.)", "CALAMBA", "CAMBINOCOT", "CAMPUTHAW (POB.)",
                                "CAPITOL SITE (POB.)", "CARRETA", "CENTRAL (POB.)", "COGON PARDO",
                                "COGON RAMOS (POB.)", "DAY-AS", "DULJO (POB.)", "ERMITA (POB.)",
                                "GUADALUPE", "GUBA", "HIPPODROMO", "INAYAWAN", "KALUBIHAN (POB.)",
                                "KALUNASAN", "KAMAGAYAN (POB.)", "KASAMBAGAN", "KINASANG-AN PARDO",
                                "LABANGON", "LAHUG (POB.)", "LOREGA (LOREGA SAN MIGUEL)", "LUSARAN",
                                "LUZ", "MABINI", "MABOLO", "MALUBOG", "MAMBALING", "PAHINA CENTRAL (POB.)",
                                "PAHINA SAN NICOLAS", "PAMUTAN", "PARDO (POB.)", "PARI-AN", "PARIL",
                                "PASIL", "PIT-OS", "PULANGBATO", "PUNG-OL-SIBUGAY", "PUNTA PRINCESA",
                                "QUIOT PARDO", "SAMBAG I (POB.)", "SAMBAG II (POB.)",
                                "SAN ANTONIO (POB.)", "SAN JOSE", "SAN NICOLAS CENTRAL",
                                "SAN ROQUE (CIUDAD)", "SANTA CRUZ (POB.)", "SAPANGDAKU",
                                "SAWANG CALERO (POB.)", "SINSIN", "SIRAO", "SUBA POB. (SUBA SAN NICOLAS)",
                                "SUDLON I", "SUDLON II", "T. PADILLA", "TABUNAN", "TAGBAO",
                                "TALAMBAN", "TAPTAP", "TEJERO (VILLA GONZALO)", "TINAGO", "TISA",
                                "TO-ONG PARDO", "ZAPATERA"));

                // --- COMPOSTELA ---
                mutableMap.put("COMPOSTELA", List.of(
                                "BAGALNGA", "BASAK", "BULUANG", "CABADIANGAN", "CAMBAYOG", "CANAMUCAN",
                                "COGON", "DAPDAP", "ESTACA", "LUPA", "MAGAY", "MULAO", "PANANGBAN",
                                "POBLACION", "TAG-UBE", "TAMIAO", "TUBIGAN"));

                // --- CONSOLACION ---
                mutableMap.put("CONSOLACION", List.of(
                                "CABANGAHAN", "CANSAGA", "CASILI", "DANGLAG", "GARING", "JUGAN",
                                "LAMAC", "LANIPGA", "NANGKA", "PANAS", "PANOYPOY", "PITOGO",
                                "POBLACION OCCIDENTAL", "POBLACION ORIENTAL", "POLOG", "PULPOGAN",
                                "SACSAC", "TAYUD", "TILHAONG", "TOLOTOLO", "TUGBONGAN"));

                // --- CORDOBA --- (Typo corrected from CORDOVA)
                mutableMap.put("CORDOBA", List.of(
                                "ALEGRIA", "BANGBANG", "BUAGSONG", "CATARMAN", "COGON", "DAPITAN",
                                "DAY-AS", "GABI", "GILUTONGAN", "IBABAO", "PILIPOG", "POBLACION",
                                "SAN MIGUEL"));

                // --- DAANBANTAYAN ---
                mutableMap.put("DAANBANTAYAN", List.of(
                                "AGUHO", "BAGAY", "BAKHAWAN", "BATERIA", "BITOON", "CALAPE", "CARNAZA",
                                "DALINGDING", "LANAO", "LOGON", "MALBAGO", "MALINGIN", "MAYA", "PAJO",
                                "PAYPAY", "POBLACION", "TALISAY", "TAPILON", "TINUBDAN", "TOMINJAO"));

                // --- DALAGUETE ---
                mutableMap.put("DALAGUETE", List.of(
                                "ABLAYAN", "BABAYONGAN", "BALUD", "BANHIGAN", "BULAK", "CALERIOHAN",
                                "CALIONGAN", "CASAY", "CATOLOHAN", "CAWAYAN", "CONSOLACION", "CORO",
                                "DUGYAN", "DUMALAN", "JOLOMAYNON", "LANAO", "LANGKAS", "LUMBANG",
                                "MALONES", "MALORAY", "MANANGGAL", "MANLAPAY", "MANTALONGON", "NALHUB",
                                "OBO", "OBONG", "PANAS", "POBLACION", "SACSAC", "SALUG", "TABON",
                                "TAPUN", "TUBA"));

                // --- DANAO CITY ---
                mutableMap.put("DANAO CITY", List.of(
                                "BALIANG", "BAYABAS", "BINALIW", "CABUNGAHAN", "CAGAT-LAMAC", "CAHUMAYAN",
                                "CAMBANAY", "CAMBUBHO", "COGON-CRUZ", "DANASAN", "DUNGGA", "DUNGGOAN",
                                "GUINACOT", "GUINSAY", "IBO", "LANGOSIG", "LAWAAN", "LICOS", "LOOC",
                                "MAGTAGOBTOB", "MALAPOC", "MANLAYAG", "MANTIJA", "MASABA", "MASLOG",
                                "NANGKA", "OGUIS", "PILI", "POBLACION", "QUISOL", "SABANG", "SACSAC",
                                "SANDAYONG NORTE", "SANDAYONG SUR", "SANTA ROSA", "SANTICAN", "SIBACAN",
                                "SUBA", "TABOC", "TAYTAY", "TOGONON", "TUBURAN SUR"));

                // --- DUMANJUG ---
                mutableMap.put("DUMANJUG", List.of(
                                "BALAYGTIKI", "BITOON", "BULAK", "BULLOGAN", "CALABOON", "CAMBOANG",
                                "CANDABONG", "COGON", "COTCOTON", "DOLDOL", "ILAYA (POB.)", "KABALAASNAN",
                                "KABATBATAN", "KAMBANOG", "KANG-ACTOL", "KANGHALO", "KANGHUMAOD",
                                "KANGUHA", "KANTANGKAS", "KANYUKO", "KOLABTINGON", "LAMAK", "LAWAAN",
                                "LIONG", "MANLAPAY", "MASA", "MATALAO", "PACULOB", "PANLAAN", "PAWA",
                                "POBLACION CENTRAL", "POBLACION LOOC", "POBLACION SIMA", "TANGIL",
                                "TAPON", "TUBOD-BITOON", "TUBOD-DUGOAN"));

                // --- GINATILAN ---
                mutableMap.put("GINATILAN", List.of(
                                "ANAO", "CAGSING", "CALABAWAN", "CAMBAGTE", "CAMPISONG", "CANORONG",
                                "GUIWANON", "LOOC", "MALATBO", "MANGACO", "PALANAS", "POBLACION",
                                "SALAMANCA", "SAN ROQUE"));

                // --- LAPU-LAPU CITY (OPON) ---
                mutableMap.put("LAPU-LAPU CITY (OPON)", List.of(
                                "AGUS", "BABAG", "BANKAL", "BARING", "BASAK", "BUAYA", "CALAWISAN",
                                "CANJULAO", "CAUBIAN", "CAW-OY", "CAWHAGAN", "GUN-OB", "IBO", "LOOC",
                                "MACTAN", "MARIBAGO", "MARIGONDON", "PAJAC", "PAJO", "PANGAN-AN",
                                "POBLACION", "PUNTA ENGAÑO", "PUSOK", "SABANG", "SAN VICENTE",
                                "SANTA ROSA", "SUBABASBAS", "TALIMA", "TINGO", "TUNGASAN"));

                // --- LILOAN ---
                mutableMap.put("LILOAN", List.of(
                                "CABADIANGAN", "CALERO", "CATARMAN", "COTCOT", "JUBAY", "LATABAN",
                                "MULAO", "POBLACION", "SAN ROQUE", "SAN VICENTE", "SANTA CRUZ",
                                "TABLA", "TAYUD", "YATI"));

                // --- MADRIDEJOS ---
                mutableMap.put("MADRIDEJOS", List.of(
                                "BUNAKAN", "KANGWAYAN", "KAONGKOD", "KODIA", "MAALAT", "MALBAGO",
                                "MANCILANG", "PILI", "POBLACION", "SAN AGUSTIN", "TABAGAK", "TALANGNAN",
                                "TARONG", "TUGAS"));

                // --- MALABUYOC ---
                mutableMap.put("MALABUYOC", List.of(
                                "ARMEÑA (CANSILONGAN)", "BARANGAY I (POB.)", "BARANGAY II (POB.)",
                                "CERDEÑA (ANSAN)", "LABRADOR (BULOD)", "LOMBO", "LOOC", "MAHANLUD",
                                "MINDANAO (PAJO)", "MONTAÑEZA (INAMLANG)", "SALMERON (BULAK)",
                                "SANTO NIÑO", "SORSOGON (BALIMAYA)", "TOLOSA (CALATAGAN)"));

                // --- MANDAUE CITY ---
                mutableMap.put("MANDAUE CITY", List.of(
                                "ALANG-ALANG", "BAKILID", "BANILAD", "BASAK", "CABANCALAN", "CAMBARO",
                                "CANDUMAN", "CASILI", "CASUNTINGAN", "CENTRO (POB.)", "CUBACUB", "GUIZO",
                                "IBABAO-ESTANCIA", "JAGOBIAO", "LABOGON", "LOOC", "MAGUIKAY",
                                "MANTUYONG", "OPAO", "PAGSABUNGAN", "PAKNA-AN", "SUBANGDAKU", "TABOK",
                                "TAWASON", "TINGUB", "TIPOLO", "UMAPAD"));

                // --- MEDELLIN ---
                mutableMap.put("MEDELLIN", List.of(
                                "ANTIPOLO", "CANHABAGAT", "CAPUTATAN NORTE", "CAPUTATAN SUR", "CURVA",
                                "DAANLUNGSOD", "DALINGDING SUR", "DAYHAGON", "DON VIRGILIO GONZALES",
                                "GIBITNGIL", "KAWIT", "LAMINTAK NORTE", "LAMINTAK SUR", "LUY-A",
                                "MAHARUHAY", "MAHAWAK", "PANUGNAWAN", "POBLACION", "TINDOG"));

                // --- MINGLANILLA ---
                mutableMap.put("MINGLANILLA", List.of(
                                "CADULAWAN", "CALAJO-AN", "CAMP 7", "CAMP 8", "CUANOS", "GUINDARUHAN",
                                "LINAO", "MANDUANG", "PAKIGNE", "POBLACION WARD I", "POBLACION WARD II",
                                "POBLACION WARD III", "POBLACION WARD IV", "TUBOD", "TULAY", "TUNGHAAN",
                                "TUNGKIL", "TUNGKOP", "VITO"));

                // --- MOALBOAL ---
                mutableMap.put("MOALBOAL", List.of(
                                "AGBALANGA", "BALA", "BALABAGON", "BASDIOT", "BATADBATAD", "BUGHO",
                                "BUGUIL", "BUSAY", "LANAO", "POBLACION EAST", "POBLACION WEST",
                                "SAAVEDRA", "TOMONOY", "TUBLE", "TUNGA"));

                // --- NAGA CITY ---
                mutableMap.put("NAGA CITY", List.of(
                                "ALFACO", "BAIRAN", "BALIRONG", "CABUNGAHAN", "CANTAO-AN",
                                "CENTRAL POBLACION", "COGON", "COLON", "EAST POBLACION", "INAYAGAN",
                                "INOBURAN", "JAGUIMIT", "LANAS", "LANGTAD", "LUTAC", "MAINIT",
                                "MAYANA", "NAALAD", "NORTH POBLACION", "PANGDAN", "PATAG",
                                "SOUTH POBLACION", "TAGJAGUIMIT", "TANGKE", "TINAAN", "TUYAN",
                                "ULING", "WEST POBLACION"));

                // --- OSLOB ---
                mutableMap.put("OSLOB", List.of(
                                "ALO", "BANGCOGON", "BONBON", "CALUMPANG", "CAN-UKBAN", "CANANGCA-AN",
                                "CANSALO-AY", "CAÑANG", "DAANLUNGSOD", "GAWI", "HAGDAN", "LAGUNDE",
                                "LOOC", "LUKA", "MAINIT", "MANLUM", "NUEVA CACERES", "POBLACION",
                                "PUNGTOD", "TAN-AWAN", "TUMALOG"));

                // --- PILAR ---
                mutableMap.put("PILAR", List.of(
                                "BIASONG", "CAWIT", "DAPDAP", "ESPERANZA", "IMELDA", "LANAO",
                                "LOWER POBLACION", "MOABOG", "MONTSERRAT", "SAN ISIDRO", "SAN JUAN",
                                "UPPER POBLACION", "VILLAHERMOSA"));

                // --- PINAMUNGAHAN --- (Typo corrected from PINAMUNGAJAN)
                mutableMap.put("PINAMUNGAHAN", List.of(
                                "ANISLAG", "ANOPOG", "BINABAG", "BUHINGTUBIG", "BUSAY", "BUTONG",
                                "CABIANGON", "CAMUGAO", "DUANGAN", "GUIMBAWIAN", "LAMAC", "LUT-OD",
                                "MANGOTO", "OPAO", "PANDACAN", "POBLACION", "PUNOD", "RIZAL", "SACSAC",
                                "SAMBAGON", "SIBAGO", "TAJAO", "TANGUB", "TANIBAG", "TUPAS", "TUTAY"));

                // --- PORO ---
                mutableMap.put("PORO", List.of(
                                "ADELA", "ALTAVISTA", "CAGCAGAN", "CANSABUSAB", "DAAN PAZ",
                                "EASTERN POBLACION", "ESPERANZA", "LIBERTAD", "MABINI", "MERCEDES",
                                "PAGSA", "PAZ", "RIZAL", "SAN JOSE", "SANTA RITA", "TEGUIS",
                                "WESTERN POBLACION"));

                // --- RONDA ---
                mutableMap.put("RONDA", List.of(
                                "BUTONG", "CAN-ABUHON", "CANDULING", "CANSALONOY", "CANSAYAHON",
                                "ILAYA", "LANGIN", "LIBO-O", "MALALAY", "PALANAS", "POBLACION",
                                "SANTA CRUZ", "TUPAS", "VIVE"));

                // --- SAMBOAN ---
                mutableMap.put("SAMBOAN", List.of(
                                "BASAK", "BONBON", "BULANGSURAN", "CALATAGAN", "CAMBIGONG", "CAMBUROY",
                                "CANORONG", "COLASE", "DALAHIKAN", "JUMANGPAS", "MONTEVERDE",
                                "POBLACION", "SAN SEBASTIAN", "SUBA", "TANGBO"));

                // --- SAN FERNANDO ---
                mutableMap.put("SAN FERNANDO", List.of(
                                "BALUD", "BALUNGAG", "BASAK", "BUGHO", "CABATBATAN", "GREENHILLS",
                                "ILAYA", "LANTAWAN", "LIBURON", "MAGSICO", "PANADTARAN", "PITALO",
                                "POBLACION NORTH", "POBLACION SOUTH", "SAN ISIDRO", "SANGAT",
                                "TABIONAN", "TANANAS", "TINUBDAN", "TONGGO", "TUBOD"));

                // --- SAN FRANCISCO ---
                mutableMap.put("SAN FRANCISCO", List.of(
                                "CABUNGA-AN", "CAMPO", "CONSUELO", "ESPERANZA", "HIMENSULAN",
                                "MONTEALEGRE", "NORTHERN POBLACION", "SAN ISIDRO", "SANTA CRUZ",
                                "SANTIAGO", "SONOG", "SOUTHERN POBLACION", "UNIDOS", "UNION",
                                "WESTERN POBLACION"));

                // --- SAN REMIGIO ---
                mutableMap.put("SAN REMIGIO", List.of(
                                "ANAPOG", "ARGAWANON", "BAGTIC", "BANCASAN", "BATAD", "BUSOGON",
                                "CALAMBUA", "CANAGAHAN", "DAPDAP", "GAWAYGAWAY", "HAGNAYA", "KAYAM",
                                "KINAWAHAN", "LAMBUSAN", "LAWIS", "LIBAONG", "LOOC", "LUYANG", "MANO",
                                "POBLACION", "PUNTA", "SAB-A", "SAN MIGUEL", "TACUP", "TAMBONGON",
                                "TO-ONG", "VICTORIA"));

                // --- SANTA FE ---
                mutableMap.put("SANTA FE", List.of(
                                "BALIDBID", "HAGDAN", "HILANTAGAAN", "KINATARKAN", "LANGUB",
                                "MARICABAN", "OKOY", "POBLACION", "POOC", "TALISAY"));

                // --- SANTANDER ---
                mutableMap.put("SANTANDER", List.of(
                                "BUNLAN", "CABUTONGAN", "CANDAMIANG", "CANLUMACAD", "LILOAN",
                                "LIP-TONG", "LOOC", "PASIL", "POBLACION", "TALISAY"));

                // --- SIBONGA ---
                mutableMap.put("SIBONGA", List.of(
                                "ABUGON", "BAE", "BAGACAY", "BAHAY", "BANLOT", "BASAK", "BATO",
                                "CAGAY", "CAN-AGA", "CANDAGUIT", "CANTOLAROY", "DUGOAN",
                                "GUIMBANGCO-AN", "LAMACAN", "LIBO", "LINDOGON", "MAGCAGONG", "MANATAD",
                                "MANGYAN", "PAPAN", "POBLACION", "SABANG", "SAYAO", "SIMALA", "TUBOD"));

                // --- SOGOD ---
                mutableMap.put("SOGOD", List.of(
                                "AMPONGOL", "BAGAKAY", "BAGATAYAM", "BAWO", "CABALAWAN", "CABANGAHAN",
                                "CALUMBOYAN", "DAKIT", "DAMOLOG", "IBABAO", "LIKI", "LUBO", "MOHON",
                                "NAHUS-AN", "PANSOY", "POBLACION", "TABUNOK", "TAKAY"));

                // --- TABOGON ---
                mutableMap.put("TABOGON", List.of(
                                "ALANG-ALANG", "CADUAWAN", "CAMOBOAN", "CANAOCANAO", "COMBADO",
                                "DAANTABOGON", "ILIHAN", "KAL-ANAN", "LABANGON", "LIBJO", "LOONG",
                                "MABULI", "MANAGASE", "MANLAGTANG", "MASLOG", "MUABOG", "PIO",
                                "POBLACION", "SALAG", "SAMBAG", "SAN ISIDRO", "SAN VICENTE", "SOMOSA",
                                "TABA-AO", "TAPUL"));

                // --- TABUELAN ---
                mutableMap.put("TABUELAN", List.of(
                                "BONGON", "DALID", "KANLIM-AO", "KANLUHANGON", "KANTUBAON", "MABUNAO",
                                "MARAVILLA", "OLIVO", "POBLACION", "TABUNOK", "TIGBAWAN", "VILLAHERMOSA"));

                // --- TALISAY CITY ---
                mutableMap.put("TALISAY CITY", List.of(
                                "BIASONG", "BULACAO", "CADULAWAN", "CAMP IV", "CANSOJONG", "DUMLOG",
                                "JACLUPAN", "LAGTANG", "LAWAAN I", "LAWAAN II", "LAWAAN III", "LINAO",
                                "MAGHAWAY", "MANIPIS", "MOHON", "POBLACION", "POOC", "SAN ISIDRO",
                                "SAN ROQUE", "TABUNOC", "TANGKE", "TAPUL"));

                // --- TOLEDO CITY ---
                mutableMap.put("TOLEDO CITY", List.of(
                                "AWIHAO", "BAGAKAY", "BATO", "BIGA", "BULONGAN", "BUNGA", "CABITOONAN",
                                "CALONGCALONG", "CAMBANG-UG", "CAMP 8", "CANLUMAMPAO", "CANTABACO",
                                "CAPITAN CLAUDIO", "CARMEN", "DAANGLUNGSOD", "DON ANDRES SORIANO (LUTOPAN)",
                                "DUMLOG", "GEN. CLIMACO (MALUBOG)", "IBO", "ILIHAN",
                                "JUAN CLIMACO, SR. (MAGDUGO)", "LANDAHAN", "LOAY", "LURAY II",
                                "MATAB-ANG", "MEDIA ONCE", "PANGAMIHAN", "POBLACION", "POOG",
                                "PUTINGBATO", "SAGAY", "SAM-ANG", "SANGI", "SANTO NIÑO (MAINGGIT)",
                                "SUBAYON", "TALAVERA", "TUBOD", "TUNGKAY"));

                // --- TUBURAN ---
                mutableMap.put("TUBURAN", List.of(
                                "ALEGRIA", "AMATUGAN", "ANTIPOLO", "APALAN", "BAGASAWE", "BAKYAWAN",
                                "BANGKITO", "BARANGAY I (POB.)", "BARANGAY II (POB.)", "BARANGAY III (POB.)",
                                "BARANGAY IV (POB.)", "BARANGAY V (POB.)", "BARANGAY VI (POB.)",
                                "BARANGAY VII (POB.)", "BARANGAY VIII (POB.)", "BULWANG", "CARIDAD",
                                "CARMELO", "COGON", "COLONIA", "DAAN LUNGSOD", "FORTALIZA", "GA-ANG",
                                "GIMAMA-A", "JAGBUAYA", "KABANGKALAN", "KABKABAN", "KAGBA-O",
                                "KALANGAHAN", "KAMANSI", "KAMPOOT", "KAN-AN", "KANLUNSING", "KANSI",
                                "KAORASAN", "LIBO", "LUSONG", "MACUPA", "MAG-ALWA", "MAG-ANTOY",
                                "MAG-ATUBANG", "MAGHAN-AY", "MANGGA", "MARMOL", "MOLOBOLO",
                                "MONTEALEGRE", "PUTAT", "SAN JUAN", "SANDAYONG", "SANTO NIÑO", "SIOTES",
                                "SUMON", "TOMINJAO", "TOMUGPA"));

                // --- TUDELA ---
                mutableMap.put("TUDELA", List.of(
                                "BUENAVISTA", "CALMANTE", "DAAN SECANTE", "GENERAL", "MCARTHUR",
                                "NORTHERN POBLACION", "PUERTOBELLO", "SANTANDER", "SECANTE BAG-O",
                                "SOUTHERN POBLACION", "VILLAHERMOSA"));

                // --- Make the map and its lists unmodifiable ---
                cebuLocations = Collections.unmodifiableMap(
                                mutableMap.entrySet().stream()
                                                .collect(Collectors.toMap(
                                                                Map.Entry::getKey,
                                                                e -> Collections.unmodifiableList(
                                                                                new ArrayList<>(e.getValue())))));
        }

        /**
         * Gets an unmodifiable list of all municipality and city names in Cebu.
         * The list is sorted alphabetically.
         *
         * @return A sorted, unmodifiable list of municipality/city names.
         */
        public List<String> getMunicipalities() {
                List<String> municipalities = new ArrayList<>(cebuLocations.keySet());
                Collections.sort(municipalities);
                return Collections.unmodifiableList(municipalities);
        }

        /**
         * Gets an unmodifiable list of barangay names for a given municipality or city.
         * Returns an empty list if the municipality/city is not found.
         *
         * @param municipalityOrCityName The exact name of the municipality or city
         *                               (case-sensitive).
         * @return An unmodifiable list of barangay names, or an empty list if not
         *         found.
         */
        public List<String> getBarangaysByMunicipality(String municipalityOrCityName) {
                // The lists in the map are already unmodifiable
                return cebuLocations.getOrDefault(municipalityOrCityName, Collections.emptyList());
        }

        /**
         * Gets the entire unmodifiable map of Cebu locations.
         * Useful if you need the whole structure at once.
         *
         * @return An unmodifiable map where keys are municipality/city names
         *         and values are unmodifiable lists of barangay names.
         */
        public Map<String, List<String>> getAllCebuLocations() {
                return cebuLocations;
        }

        /**
         * Finds the municipality or city that contains the given barangay.
         *
         * @param barangayName The barangay name to look for.
         * @return The municipality/city name, or "Unknown" if not found.
         */
        public String getMunicipalityByBarangay(String barangayName) {
                return cebuLocations.entrySet().stream()
                                .filter(entry -> entry.getValue().contains(barangayName))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse("Unknown");
        }
}