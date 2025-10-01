from fastapi import FastAPI
from pydantic import BaseModel
import joblib

# Load model
model = joblib.load("model.pkl")

app = FastAPI()

class CropRequest(BaseModel):
    N: int
    P: int
    K: int
    temperature: float
    humidity: float
    ph: float
    rainfall: float

@app.post("/predict")
def predict_crop(data: CropRequest):
    features = [[
        data.N, data.P, data.K, data.temperature,
        data.humidity, data.ph, data.rainfall
    ]]

    prediction = model.predict(features)[0]
    probabilities = model.predict_proba(features)[0]
    confidence = max(probabilities)

    return {
        "recommended_crop": prediction,
        "probability": float(confidence)
    }
