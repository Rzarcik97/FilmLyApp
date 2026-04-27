interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
}

export const Modal = ({ isOpen, onClose, title, children }: ModalProps) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center p-4">
      <div className="absolute inset-0 bg-black/60 backdrop-blur-sm" onClick={onClose} />

      <div className="relative bg-gray-dark border border-gray-80 w-full max-w-md rounded-[24px] p-6 md:p-8 shadow-2xl">
        <h3 className="text-white text-[20px] md:text-[24px] font-bold mb-6">{title}</h3>

        {children}

        <div className="flex gap-3 mt-8">
          <button
            onClick={onClose}
            className="flex-1 py-3 rounded-full border border-gray-70 text-white font-bold hover:bg-white/5"
          >
            Cancel
          </button>
          <button
            className="flex-1 py-3 rounded-full bg-primary-0 text-gray-dark font-bold hover:brightness-110"
          >
            Save Changes
          </button>
        </div>
      </div>
    </div>
  );
};