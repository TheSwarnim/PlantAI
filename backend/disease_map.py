disease = {'Apple_Applescab': 0,
 'AppleBlack_rot': 1,
 'Apple_Cedar_applerust': 2,
 'Applehealthy': 3,
 'Blueberryhealthy': 4,
 'Cherry(includingsour)Powderymildew': 5,
 'Cherry(including_sour)healthy': 6,
 'Corn(maize)_Cercospora_leaf_spot Gray_leafspot': 7,
 'Corn(maize)_Commonrust': 8,
 'Corn(maize)Northern_LeafBlight': 9,
 'Corn(maize)healthy': 10,
 'GrapeBlack_rot': 11,
 'GrapeEsca(BlackMeasles)': 12,
 'GrapeLeafblight(Isariopsis_Leaf_Spot)': 13,
 'Grapehealthy': 14,
 'OrangeHaunglongbing_(Citrus_greening)': 15,
 'Peach_Bacterialspot': 16,
 'Peachhealthy': 17,
 'Pepper,_bell_Bacterial_spot': 18,
 'Pepper,bellhealthy': 19,
 'Potato_Earlyblight': 20,
 'PotatoLate_blight': 21,
 'Potatohealthy': 22,
 'Raspberryhealthy': 23,
 'Soybeanhealthy': 24,
 'SquashPowdery_mildew': 25,
 'Strawberry_Leafscorch': 26,
 'Strawberryhealthy': 27,
 'Tomato_Bacterialspot': 28,
 'TomatoEarly_blight': 29,
 'Tomato_Lateblight': 30,
 'TomatoLeaf_Mold': 31,
 'Tomato_Septoria_leafspot': 32,
 'TomatoSpider_mites Two-spotted_spider_mite': 33,
 'Tomato_TargetSpot': 34,
 'TomatoTomato_Yellow_Leaf_Curl_Virus': 35,
 'Tomato_Tomato_mosaicvirus': 36,
 'Tomatohealthy': 37}

# to generate disease map from above map
# disease_map = {v: k for k, v in my_map.items()}

disease_map = {
    0: 'Apple_Applescab', 
    1: 'AppleBlack_rot', 
    2: 'Apple_Cedar_applerust', 
    3: 'Applehealthy', 
    4: 'Blueberryhealthy', 
    5: 'Cherry(includingsour)Powderymildew', 
    6: 'Cherry(including_sour)healthy', 
    7: 'Corn(maize)_Cercospora_leaf_spot Gray_leafspot', 
    8: 'Corn(maize)_Commonrust', 
    9: 'Corn(maize)Northern_LeafBlight', 
    10: 'Corn(maize)healthy', 
    11: 'GrapeBlack_rot', 
    12: 'GrapeEsca(BlackMeasles)', 
    13: 'GrapeLeafblight(Isariopsis_Leaf_Spot)', 
    14: 'Grapehealthy', 
    15: 'OrangeHaunglongbing_(Citrus_greening)', 
    16: 'Peach_Bacterialspot', 
    17: 'Peachhealthy', 
    18: 'Pepper,_bell_Bacterial_spot', 
    19: 'Pepper,bellhealthy', 
    20: 'Potato_Earlyblight', 
    21: 'PotatoLate_blight', 
    22: 'Potatohealthy', 
    23: 'Raspberryhealthy', 
    24: 'Soybeanhealthy', 
    25: 'SquashPowdery_mildew', 
    26: 'Strawberry_Leafscorch', 
    27: 'Strawberryhealthy', 
    28: 'Tomato_Bacterialspot', 
    29: 'TomatoEarly_blight', 
    30: 'Tomato_Lateblight', 
    31: 'TomatoLeaf_Mold', 
    32: 'Tomato_Septoria_leafspot', 
    33: 'TomatoSpider_mites Two-spotted_spider_mite', 
    34: 'Tomato_TargetSpot', 
    35: 'TomatoTomato_Yellow_Leaf_Curl_Virus', 
    36: 'Tomato_Tomato_mosaicvirus', 
    37: 'Tomatohealthy'
}