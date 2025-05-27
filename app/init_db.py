from core.database import engine, Base
from models import user, cloth, post

def init_db():
    # user.Base.metadata.create_all(bind=engine)
    # cloth.Base.metadata.create_all(bind=engine)
    # post.Base.metadata.create_all(bind=engine)
    Base.metadata.create_all(bind=engine)

if __name__ == "__main__":
    init_db()
    print("Database initialized successfully!") 