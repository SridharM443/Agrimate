from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware
import joblib

# Load model
model = joblib.load("model.pkl")

app = FastAPI()

# ✅ Enable CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # or ["https://your-frontend.onrender.com"]
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

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
        "recommendedCrop": prediction,
        "probability": float(confidence)
    }

@app.get("/")
def home():
    return {"message": "Welcome to the Agrimate ML API 🚀. Use /predict to get crop recommendations."}
