import sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from sqlalchemy import Column, Integer, String, DateTime
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from datetime import datetime
from core.database import Base, engine

Base.metadata.create_all(bind=engine)

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(50), unique=True, index=True)
    gender = Column(String(10))
    birthDate = Column(DateTime)
    authToken = Column(String(100))
    hashed_password = Column(String(100))
    created_at = Column(DateTime(timezone=True), server_default=func.now())
    updated_at = Column(DateTime(timezone=True), onupdate=func.now())

    # Relationships
    posts = relationship("Post", back_populates="user")
    clothes = relationship("Cloth", back_populates="user")