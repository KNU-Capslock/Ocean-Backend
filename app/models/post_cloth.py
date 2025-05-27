import sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from sqlalchemy import Column, Integer, ForeignKey
from sqlalchemy.orm import relationship

from core.database import Base, engine
# Base.metadata.create_all(bind=engine)
class PostCloth(Base):
    __tablename__ = "posts_clothes"

    id = Column(Integer, primary_key=True, index=True)
    cloth_id = Column(Integer, ForeignKey("Cloth.id"))
    post_id = Column(Integer, ForeignKey("Post.id"))
    user_id = Column(Integer, ForeignKey("User.id"))

    # Relationships
    post = relationship("Post", back_populates="clothes")
    cloth = relationship("Cloth", back_populates="posts") 