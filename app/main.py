from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api import users, clothes, posts

app = FastAPI(
    title="Ocean Backend",
    description="Ocean Backend API",
    version="1.0.0"
)

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 라우터 등록
app.include_router(users.router, prefix="", tags=["users"])
app.include_router(clothes.router, prefix="", tags=["clothes"])
app.include_router(posts.router, prefix="", tags=["posts"])

@app.get("/")
async def root():
    return {"message": "Welcome to Ocean Backend API"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="localhost", port=8000)