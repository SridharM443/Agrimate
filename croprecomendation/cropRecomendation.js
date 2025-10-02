 document.getElementById("cropForm").addEventListener("submit", async function(e) {
    e.preventDefault();

    const data = {
      N: Number(document.getElementById("nitrogen").value),
      P: Number(document.getElementById("phosphorus").value),
      K: Number(document.getElementById("potassium").value),
      temperature: Number(document.getElementById("temperature").value),
      humidity: Number(document.getElementById("humidity").value),
      ph: Number(document.getElementById("ph").value),
      rainfall: Number(document.getElementById("rainfall").value)
    };

    try {
      const response = await fetch("https://agrimate-springboot.onrender.com/api/crops/predict", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      });

      const result = await response.json();

      const cropName = result.recommendedCrop || "Unknown";
      const probability = result.probability !== undefined ? (result.probability * 100).toFixed(2) + "%" : "N/A";

      document.getElementById("resultBox").style.display = "block";
      document.getElementById("resultBox").innerText =
        "✅ Recommended Crop: 🌾 " + cropName + " (Confidence: " + probability + ")";
    } catch (error) {
      document.getElementById("resultBox").style.display = "block";
      document.getElementById("resultBox").innerText = "❌ Error fetching recommendation: " + error;
      console.error("Fetch Error:", error);
    }

  });
