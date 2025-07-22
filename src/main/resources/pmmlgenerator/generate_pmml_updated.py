import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn2pmml import PMMLPipeline, sklearn2pmml

# Sample dataset
data = pd.DataFrame({
    "area_ha": [1.0, 2.0, 3.0, 4.0, 5.0, 6.0],
    "fertilizer_n_kg_per_ha": [50, 100, 150, 200,250,300],
    "diesel_liters": [100, 200, 300, 400, 500, 600],
   "latitude":[19.07, 20.07, 22.07, 24.07, 26.07, 28.07],
   "longitude":[72.88, 74.88, 76.88, 78.88, 80.88, 82.88],
    "carbon_offset_tco2e": [1.2, 2.5, 3.8, 5.1, 6.4, 7.7]
})

X = data[["area_ha", "fertilizer_n_kg_per_ha", "diesel_liters","latitude","longitude"]]
y = data["carbon_offset_tco2e"]

# Create a pipeline
pipeline = PMMLPipeline([
    ("scaler", StandardScaler()),
    ("regressor", RandomForestRegressor(n_estimators=10, random_state=42))
])

pipeline.fit(X, y)

# Export to PMML
sklearn2pmml(pipeline, "carbon_model.pmml", with_repr=True)
print("PMML file generated: carbon_model.pmml")
