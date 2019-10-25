import csv
import os
import re

# Point the DATA_DIR to where your csv files are 
DATA_DIR = 'data'
LELY_OUTFILE = 'lely_combined.csv'
ROVECOM_OUTFILE = 'rovecom_combined.csv'

ROVECOM_PATTERN = r'(\d+)_(R|r)ovecom.csv'
LELY_PATTERN = r'Lely_dataset_\[(\d+)\].csv'

LELY_COLNAMES = [
"FarmNumber","Customer","LifeNumber","Date_yyyymmdd","Lactation","LactationDays","CalvingDate_yyyymmdd","DryOffDate_yyyymmdd","TransferDate_yyyymmdd","DayProduction","Mastitis","Colostrum","ConductivityAttentions","SCC","FatIndication","ProteinIndication","Weight","Milkings","Refusals","Failures","MilkTimeAvg","DeadMilkTimeAvg","MilkSpeed","Rumination","Feed_1","Feed_2","Feed_3","Feed_4","AmountSet1","AmountSet2","AmountSet3","AmountSet4","TotalIntake1","TotalIntake2","TotalIntake3","TotalIntake4","RestFeed1","RestFeed2","RestFeed3","RestFeed4"]

ROVECOM_COLNAMES = [
"FarmNumber", "DateFrom", "DateTo", "Levensnummer", "Naam", "VEM", "Diernummer", "Fasenr", "BZET", "DS", "DSBasisr","DSRv", "DSKVA", "KV1", "KV2", "KV3","KVKVA","Meet","OEB","Pg","Prodopn","RC","RE","Ruwvoer","Suiker","SWoud","VEM","Zetmeel","VEM BR" 
]

f_lely = open(LELY_OUTFILE, 'w')
lely_writer = csv.writer(f_lely, delimiter=",")
# Write the header
lely_writer.writerow(LELY_COLNAMES)

f_rovecom = open(ROVECOM_OUTFILE, 'w')
rovecom_writer = csv.writer(f_rovecom, delimiter=",")
rovecom_writer.writerow(ROVECOM_COLNAMES)

for filename in os.listdir(DATA_DIR):
    # Process Rovecom files
    m = re.match(ROVECOM_PATTERN, filename)
    if m is not None:
        farmno = m.group(1)
        with open(os.path.join(DATA_DIR, filename)) as f:
            print("Processing " + filename)
            reader = csv.reader(f, delimiter=";")
            # Skip the header
            for i in range(3):
                next(reader, None)
            for row in reader:
                # Select only relevant columns, add farmo as first column
                outrow = [farmno] \
                  + row[:13] \
                  + row[14:15] \
                  + row[16:17] \
                  + row[18:31]
                # Replace commas with dots
                for i in range(len(outrow)):
                    outrow[i] = outrow[i].replace(",",".")
                rovecom_writer.writerow(outrow)

    # Process Lely files
    m = re.match(LELY_PATTERN, filename)
    if m is not None:
        farmno = m.group(1)
        with open(os.path.join(DATA_DIR, filename)) as f:
            print("Processing " + filename)
            reader = csv.reader(f)
            # skip the header
            next(reader, None)
            for row in reader:
                lely_writer.writerow([farmno] + row[1:])


f_lely.close()                
f_rovecom.close()
