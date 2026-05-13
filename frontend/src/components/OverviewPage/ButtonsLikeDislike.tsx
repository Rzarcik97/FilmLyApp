import thumbsUp from '/icons/likes/like.svg';
import thumbsUpPrimary from '/icons/likes/likePrimary.svg';
import thumbsDown from '/icons/likes/dislike.svg';
import thumbsDownPrimary from '/icons/likes/dislikePrimary.svg';
import { useDispatch, useSelector } from 'react-redux';
import type { RootState } from '../../store';
import { toggleLike } from '../../api/movieService';
import { updateStats } from '../../store/likesSlice';
import { AuthModal } from '../Modals/AuthModal';
import { openAuthModal } from '../../store/uiSlice';

interface ButtonsLikeDislikeProps {
  contentId: number | string;
  contentType: string;
}

export const ButtonsLikeDislike = ({ contentId, contentType }: ButtonsLikeDislikeProps) => {
  const dispatch = useDispatch();
  const idKey = String(contentId);

  const itemData = useSelector((state: RootState) => state.likes.items[idKey]);

  const isLoggedIn = !!localStorage.getItem('token');

  const handleToggle = async (isLike: boolean) => {
    if (!isLoggedIn) {
      dispatch(openAuthModal());
      return;
    }

    try {
      const data = await toggleLike(contentId, contentType, isLike);

      const currentReaction = itemData?.userReaction;
      const nextReaction = (isLike && currentReaction === 'LIKE') || (!isLike && currentReaction === 'DISLIKE')
        ? null
        : (isLike ? 'LIKE' : 'DISLIKE');

      dispatch(updateStats({
        id: idKey,
        likes: data.likes,
        dislikes: data.dislikes,
        reaction: nextReaction
      }));
    } catch (error) {
      console.error("Redux Like Error:", error);
    }
  };

  return (
    <div className="hidden md:flex justify-center items-center gap-6">
      <button
        onClick={() => handleToggle(true)}
        className="cursor-pointer w-16 h-16 flex justify-center items-center gap-1
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          ">
        <img
          src={itemData?.userReaction === 'LIKE' ? thumbsUpPrimary : thumbsUp}
          alt="Likes"
          className="w-6 h-6 object-cover"
        />
        <span className="text-gray-0 text-[12px] font-semibold font-nunito">{itemData?.likes ?? 0}</span>
      </button>
      <button
        onClick={() => handleToggle(false)}
        className="cursor-pointer w-16 h-16 flex justify-center items-center gap-1
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          ">
        <img
          src={itemData?.userReaction === 'DISLIKE' ? thumbsDownPrimary : thumbsDown}
          alt="Dislikes"
          className="w-6 h-6 object-cover"
        />
        <span className="text-gray-0 text-[12px] font-semibold font-nunito">{itemData?.dislikes ?? 0}</span>
      </button>

      <AuthModal />
    </div>
  )
}