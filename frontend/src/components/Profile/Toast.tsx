export const Toast = ({ isVisible }: { isVisible: boolean }) => {
  return (
    <div className={`fixed bottom-10 left-1/2 -translate-x-1/2 z-[200] transition-all duration-500
      ${isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4 pointer-events-none'}`}>
      <div className="bg-gray-90 border border-gray-80 text-white px-6 py-3 rounded-full shadow-2xl flex items-center gap-3">
        <div className="w-2 h-2 rounded-full bg-primary-0 animate-pulse" />
        <span className="font-nunito text-sm md:text-base">Sorry, this feature is not implemented yet</span>
      </div>
    </div>
  );
};