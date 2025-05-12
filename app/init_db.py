from app.database import engine
from app.models import user, cloth, post

def init_db():
    user.Base.metadata.create_all(bind=engine)
    cloth.Base.metadata.create_all(bind=engine)
    post.Base.metadata.create_all(bind=engine)

if __name__ == "__main__":
    init_db()
    print("Database initialized successfully!") 