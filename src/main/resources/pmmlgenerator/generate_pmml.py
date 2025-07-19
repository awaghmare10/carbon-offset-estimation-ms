import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn2pmml import PMMLPipeline, sklearn2pmml

# Load dataset
data = pd.read_csv("carbon_data.csv")
X = data[["area_ha", "fertilizer_n_kg_per_ha", "diesel_liters"]]
y = data["carbon_offset_tco2e"]

# Create pipeline
pipeline = PMMLPipeline([
    ("scaler", StandardScaler()),
    ("regressor", RandomForestRegressor(n_estimators=10, random_state=42))
])
pipeline.fit(X, y)

# Export to PMML
sklearn2pmml(pipeline, "carbon_model.pmml", with_repr=True)
print("PMML file generated successfully.")
