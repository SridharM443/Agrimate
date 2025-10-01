import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
import joblib
import argparse
import os

def load_data(path):
    return pd.read_csv(path)

def train_and_save_model(data_path, outdir=".", test_size=0.2, seed=42):
    df = load_data(data_path)

    X = df.drop("label", axis=1)
    y = df["label"]

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=test_size, random_state=seed
    )

    model = RandomForestClassifier(n_estimators=100, random_state=seed)
    model.fit(X_train, y_train)

    preds = model.predict(X_test)
    acc = accuracy_score(y_test, preds)
    print(f"✅ Model trained with accuracy: {acc*100:.2f}%")

    os.makedirs(outdir, exist_ok=True)
    model_path = os.path.join(outdir, "model.pkl")
    joblib.dump(model, model_path)
    print(f"📦 Model saved to {model_path}")

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--data", required=True, help="Path to dataset CSV")
    parser.add_argument("--outdir", default=".", help="Where to save model")
    args = parser.parse_args()

    train_and_save_model(args.data, args.outdir)

if __name__ == "__main__":
    main()
