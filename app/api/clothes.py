from fastapi import APIRouter, Depends, HTTPException, UploadFile, File, status, Form
# from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from typing import List, Optional
import os
import uuid

from core.database import get_db
from schemas.cloth import Cloth, ClothCreate, ClothUpdate
from crud import cloth as cloth_crud
from crud import user as user_crud
# from core.security import verify_token

router = APIRouter()
# oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

# 이미지 저장 디렉토리 설정
UPLOAD_DIR = os.path.join(os.path.dirname(os.path.dirname(os.path.dirname(__file__))), "uploads", "images")
os.makedirs(UPLOAD_DIR, exist_ok=True)

@router.post("/clothes", response_model=Cloth)
async def create_cloth(
    file: UploadFile = File(...),
    type: str = Form(...),  # Form 데이터로 변경
    detail: Optional[str] = Form(None),  # Form 데이터로 변경
    print: Optional[str] = Form(None),   # Form 데이터로 변경
    texture: Optional[str] = Form(None), # Form 데이터로 변경
    style: Optional[str] = Form(None),   # Form 데이터로 변경
    # token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    # Verify token and get user
    # payload = verify_token(token)
    # username = payload.get("sub")
    # user = user_crud.get_user_by_name(db, username=username)
    # if not user:
    #     raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")

    # 필수 필드 검증
    if not type:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Type is required"
        )

    # 이미지 파일 확장자 검증
    allowed_extensions = {".jpg", ".jpeg", ".png", ".gif"}
    file_ext = os.path.splitext(file.filename)[1].lower()
    if file_ext not in allowed_extensions:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Invalid file type. Only jpg, jpeg, png, and gif files are allowed."
        )

    # 고유한 파일명 생성 (UUID + 원본 확장자)
    unique_filename = f"{uuid.uuid4()}{file_ext}"
    file_location = os.path.join(UPLOAD_DIR, unique_filename)
    
    # 이미지 파일 저장
    try:
        contents = await file.read()
        with open(file_location, "wb") as f:
            f.write(contents)
        await file.seek(0)
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to save image file: {str(e)}"
        )
    
    cloth_data = ClothCreate(
        type=type,
        detail=detail or "",  # NULL 대신 빈 문자열 사용
        print=print or "",    # NULL 대신 빈 문자열 사용
        texture=texture or "", # NULL 대신 빈 문자열 사용
        style=style or "",    # NULL 대신 빈 문자열 사용
        user_id=1  # 임시로 user_id=1 사용
    )
    
    return cloth_crud.create_cloth(db=db, cloth=cloth_data, image_src=file_location)

@router.get("/clothes", response_model=List[Cloth])
def get_user_clothes(
    # token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    # payload = verify_token(token)
    # username = payload.get("sub")
    # user = user_crud.get_user_by_name(db, username=username)
    # if not user:
    #     raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
    
    return cloth_crud.get_clothes_by_user(db=db, user_id=1)  # 임시로 user_id=1 사용

@router.get("/clothes/{cloth_id}", response_model=Cloth)
def get_cloth(
    cloth_id: int,
    # token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    # payload = verify_token(token)
    # username = payload.get("sub")
    # user = user_crud.get_user_by_name(db, username=username)
    # if not user:
    #     raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")

    cloth = cloth_crud.get_cloth(db=db, cloth_id=cloth_id)
    if not cloth:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Cloth not found")
    
    # if cloth.user_id != user.id:
    #     raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Not authorized to access this cloth")
    
    return cloth

@router.patch("/clothes/{cloth_id}", response_model=Cloth)
def update_cloth(
    cloth_id: int,
    cloth_update: ClothUpdate,
    # token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    # payload = verify_token(token)
    # username = payload.get("sub")
    # user = user_crud.get_user_by_name(db, username=username)
    # if not user:
    #     raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")

    cloth = cloth_crud.get_cloth(db=db, cloth_id=cloth_id)
    if not cloth:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Cloth not found")
    
    # if cloth.user_id != user.id:
    #     raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Not authorized to update this cloth")
    
    return cloth_crud.update_cloth(db=db, cloth_id=cloth_id, cloth=cloth_update)

@router.delete("/clothes/{cloth_id}")
def delete_cloth(
    cloth_id: int,
    # token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    # payload = verify_token(token)
    # username = payload.get("sub")
    # user = user_crud.get_user_by_name(db, username=username)
    # if not user:
    #     raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")

    cloth = cloth_crud.get_cloth(db=db, cloth_id=cloth_id)
    if not cloth:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Cloth not found")
    
    # if cloth.user_id != user.id:
    #     raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Not authorized to delete this cloth")
    
    # 옷 삭제 시 이미지 파일도 함께 삭제
    try:
        if os.path.exists(cloth.image_src):
            os.remove(cloth.image_src)
    except Exception:
        pass
    
    success = cloth_crud.delete_cloth(db=db, cloth_id=cloth_id)
    if not success:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Cloth not found")
    return {"message": "Cloth deleted successfully"} 