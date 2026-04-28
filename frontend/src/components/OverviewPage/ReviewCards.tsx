import pic_1 from '../../../public/reviews/pic_1.png';
import pic_2 from '../../../public/reviews/pic_2.png';
import pic_3 from '../../../public/reviews/pic_3.png';
import pic_4 from '../../../public/reviews/pic_4.png';
import rating from '../../../public/reviews/rating.svg';

const MOCK_REVIEWS = [
  { 
    id: 1,
    profilePic: pic_1,
    name: 'Aelin Rodriguez',
    text: "This is an engagingly simple, good-hearted film, with just enough darkness around the edges to give contrast and relief to its glowingly benign view of human nature.",
    date: 'Thuesday'
  },
  { 
    id: 2,
    profilePic: pic_2,
    name: 'Sarah Ahmad',
    text: "A tough, complex story [told] with clarity, compassion and considerable dramatic force.",
    date: 'March 22, 2024'
  },
  { 
    id: 3,
    profilePic: pic_3,
    name: 'Abdullah Sharief',
    text: "It doesn't matter how we think we remember the moments, how they look now, in this edition, will force a shift in memory that we'll most likely be grateful for.",
    date: 'September 16, 2023'
  },
  { 
    id: 4,
    profilePic: pic_4,
    name: 'Marshall Jenkins',
    text: "Finally, a film that treats the audience with intelligence! No over-explaining the plot, just pure visual storytelling. Filmly really nailed it with this recommendation.",
    date: 'August 10, 2023'
  }
]

export const ReviewCards = () => {
  return (
    <div className="flex justify-between items-start px-14">
      {MOCK_REVIEWS.map(review => (
        <div 
          key={review.id}
          className={`p-3 bg-gray-90 flex flex-col gap-[11px] rounded-[8px] w-80
            ${review.id === 1 ? 'flex' : 'hidden'}
            ${review.id === 2 ? 'md:flex' : ''}
            ${review.id > 2 ? 'lg:flex' : ''}
            `}
        >
          <div className="flex gap-[5px] items-center">
            <img src={review.profilePic} alt="Profile pic" className="w-10 h-10 rounded-full"/>
            <div className="flex flex-col items-start">
              <h2 className="font-nunito text-secondary-light text-[24px] leading-[1.3] font-bold">{review.name}</h2>
              <img src={rating} alt="Rating" className="w-[53px]"/>
            </div>
          </div>
          <p className="font-nunito text-secondary-light text-[16px] leading-[1.35]">{review.text}</p>
          <p className="text-gray-30 text-[12px] text-right">{review.date}</p>
        </div>
      ))}
    </div>
  )
}