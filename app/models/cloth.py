import sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from sqlalchemy import Column, Integer, String, DateTime, ForeignKey, Table, Text
from sqlalchemy.sql import func
from sqlalchemy.orm import relationship
from core.database import Base, engine
from datetime import datetime

# Association table for many-to-many relationship between posts and clothes
posts_clothes = Table(
    "posts_clothes",
    Base.metadata,
    Column("post_id", Integer, ForeignKey("posts.id", ondelete="CASCADE"), primary_key=True),
    Column("cloth_id", Integer, ForeignKey("clothes.id", ondelete="CASCADE"), primary_key=True)
)

class Cloth(Base):
    __tablename__ = "clothes"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), nullable=False)
    type = Column(String(50), nullable=False)
    detail = Column(String(100), nullable=False)
    print = Column(String(50), nullable=False)
    texture = Column(String(50), nullable=False)
    style = Column(String(50), nullable=False)
    image_src = Column(String(255), nullable=False)
    created_at = Column(DateTime(timezone=True), server_default=func.now(), nullable=False)
    updated_at = Column(DateTime(timezone=True), server_default=func.now(), onupdate=func.now(), nullable=False)

    # Relationships
    user = relationship("User", back_populates="clothes")
    posts = relationship("Post", secondary=posts_clothes, back_populates="clothes")

# Base.metadata.create_all(bind=engine) 