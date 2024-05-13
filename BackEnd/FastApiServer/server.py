# from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
# import recommendation
# import asyncio
# import requests
# import subprocess
import spark_reco

# app = FastAPI()



# @app.get("/")
# async def root():
#     return {"message": "Hello World"}


# @app.get("/hello/{name}")
# async def say_hello(name: str):
#     return {"message": f"Hello {name}"}

from fastapi import FastAPI, Request
from pyspark.sql import SparkSession
from pyspark.sql.functions import lit
import json

app = FastAPI()

# # Spark 세션 초기화
# spark = SparkSession.builder \
#     .appName("FastAPI-Spark Integration") \
#     .getOrCreate()

class UserRequest(BaseModel):
    userId: int


@app.post("/recommend")
def recommend_commercial_areas(request: UserRequest):
    print("추천에 도착!")
    return spark_reco.recommend_commercials(request.userId)

@app.post("/data")
async def receive_data(request: Request):
    body = await request.body()
    data_str = body.decode('utf-8')  # 바이트를 문자열로 디코딩
    data = json.loads(data_str)      # 문자열을 JSON(딕셔너리)으로 변환
    print("Received data:", data)
    return {"message": "Data received successfully", "receivedData": data}

# @app.get("/test-spark")
# def test_spark():
#     # 데이터 프레임 생성
#     data = [("Java", 20000), ("Python", 100000), ("Scala", 3000)]
#     columns = ["Language", "Users"]
#     df = spark.createDataFrame(data, schema=columns)

#     # 간단한 데이터 변환 수행
#     df = df.withColumn("UsersPlusOne", df["Users"] + lit(1))

#     # 결과를 JSON 형식으로 변환하여 반환
#     result = df.toPandas().to_dict(orient="records")  # pandas를 사용하여 데이터 프레임을 딕셔너리로 변환
#     return {"result": result}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)