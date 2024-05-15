import List from '@mui/joy/List'
import ListItem from '@mui/joy/ListItem'
import FormControl from '@mui/joy/FormControl'
import FormLabel from '@mui/joy/FormLabel'
import Switch from '@mui/joy/Switch'
import Modal from '@mui/joy/Modal'
import ModalDialog, { ModalDialogProps } from '@mui/joy/ModalDialog'
import ModalClose from '@mui/joy/ModalClose'
import DialogTitle from '@mui/joy/DialogTitle'
import { useState, useEffect } from 'react'

interface ModalType {
  layout: ModalDialogProps['layout'] | undefined
  setLayout: (layout: ModalDialogProps['layout']) => void
  // scroll: boolean
  // setScroll: (scroll: boolean) => void
}

const SimulationReportCompare = ({ layout, setLayout }: ModalType) => {
  const [scroll, setScroll] = useState<boolean>(true)

  useEffect(() => {
    setLayout('center')
  }, [])

  return (
    <Modal
      style={{ zIndex: 1300 }}
      open={!!layout}
      onClose={() => {
        setLayout(undefined)
      }}
    >
      <ModalDialog layout={layout}>
        <ModalClose />
        <DialogTitle>Vertical scroll example</DialogTitle>
        <FormControl
          orientation="horizontal"
          sx={{ bgcolor: 'background.level2', p: 1, borderRadius: 'sm' }}
        >
          <FormLabel>Container overflow</FormLabel>
          <Switch
            checked={scroll}
            onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
              setScroll(event.target.checked)
            }
            sx={{ ml: 'auto' }}
          />
        </FormControl>
        <List
          sx={{
            overflow: scroll ? 'scroll' : 'initial',
            mx: 'calc(-1 * var(--ModalDialog-padding))',
            px: 'var(--ModalDialog-padding)',
          }}
        >
          {[...Array(100)].map((_item, index) => (
            <ListItem key={index}>I&apos;m in a scrollable area.</ListItem>
          ))}
        </List>
      </ModalDialog>
    </Modal>
  )
}

export default SimulationReportCompare
